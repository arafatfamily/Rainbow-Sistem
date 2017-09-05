package com.bitsolution.pln.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bitsolution.pln.Helpers.AppConfig;
import com.bitsolution.pln.Helpers.SQLiteDBHelper;
import com.bitsolution.pln.Helpers.SQLiteHandler;
import com.bitsolution.pln.Helpers.SessionManager;
import com.bitsolution.pln.MainActivity;
import com.bitsolution.pln.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by GILBERT on 25/08/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText mPhoneNumber;
    private String TAG = LoginActivity.class.getSimpleName();
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            redirectToMainActivity();
        }

        mPhoneNumber = (EditText) findViewById(R.id.input_phone);
        if (AppConfig.GETPHONE_NUMBER(getApplicationContext()) != null) {
            mPhoneNumber.setText(AppConfig.GETPHONE_NUMBER(getApplicationContext()));
        }
        Button mLogin = (Button) findViewById(R.id.btn_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhoneNumber.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Nomor HP tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else {
                    String iNumber = mPhoneNumber.getText().toString().trim();
                    sendPhoneToSvr(iNumber);
                }
            }
        });
    }

    private void sendPhoneToSvr(String phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("_api", "hp");
        params.put("_data", phone);
        params.put("_tipehp", Build.MANUFACTURER + "-" + Build.MODEL);
        params.put("_oshp", Build.VERSION.RELEASE);
        params.put("_memhp", AppConfig.FREE_STORAGE());
        params.put("_ramhp", AppConfig.TOTAL_RAM());
        AppConfig.GETJSON("GET", params, new AppConfig.onRespOK() {
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONArray json = new JSONArray(result);
                    //saveUserDataToLocalStorage(json.getJSONObject(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new AppConfig.onRespFail() {
            @Override
            public void onFailResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                //Fail Response ... do something with error return.
            }
        });
    }

    private void redirectToMainActivity() {
        //session.setLogin(true);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveUserDataToLocalStorage(JSONObject json) {
        SharedPreferences DT_USER = getApplicationContext().getSharedPreferences(AppConfig.DB_USER_PREF, 0);
        SharedPreferences.Editor uEdit = DT_USER.edit();
        try {
            uEdit.putString("KODE_CATTER", json.getString("KODE_CATTER"));
            uEdit.putString("NAMA_CATTER", json.getString("NAMA_CATTER"));
            uEdit.putString("KODE_AREA", json.getString("KODE_AREA"));
            uEdit.putString("NOMOR_HP", json.getString("NOMOR_HP"));
            uEdit.putString("APP_CODE", json.getString("APP_CODE"));
            uEdit.putString("USER_ROLE", json.getString("USER_ROLE"));
            uEdit.apply();
            getUserAttributes(SQLiteDBHelper.Table_Menu, json.getString("NOMOR_HP"), json.getString("APP_CODE"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserAttributes(final String mode, final String nomor_hp, final String app_code) {
        Map<String, Object> params = new HashMap<>();
        params.put("_api", mode);
        params.put("_data", SQLiteDBHelper.DT_REQ);
        params.put("_APPCODE", app_code);
        params.put("_hp", nomor_hp);

        AppConfig.GETJSON("GET", params, new AppConfig.onRespOK() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccessResponse(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    SQLiteDBHelper.BatchSaveDataToSQLite(mode, jsonArray.getJSONObject(0));
                    if (Objects.equals(mode, SQLiteDBHelper.Table_Menu)) { // Requires KITKAT(SDK 19/4.4) Devices
                        getUserAttributes(SQLiteDBHelper.Table_Label, nomor_hp, app_code);
                    } else {
                        downloadRequirementData(app_code);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new AppConfig.onRespFail() {
            @Override
            public void onFailResponse(VolleyError response) {
                getUserAttributes(mode, nomor_hp, app_code);
            }
        });
    }

    private void downloadRequirementData(String app_code) {

    }
}
