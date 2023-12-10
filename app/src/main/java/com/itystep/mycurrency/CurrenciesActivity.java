package com.itystep.mycurrency;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CurrenciesActivity  extends AppCompatActivity {
    TextView  usdBuyText,evrBuyText,gbpBuyText,plnBuyText;
    TextView usdSellText,evrSellText,gbpSellText,plnSellText;
    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_currensys);
        Intent intent=getIntent();
        usdBuyText=findViewById(R.id.usd_buy);
        usdSellText=findViewById(R.id.usd_sell);
        gbpBuyText=findViewById(R.id.gbp_buy);
        gbpSellText=findViewById(R.id.gbp_sell);
        usdBuyText=findViewById(R.id.usd_buy);
        usdSellText=findViewById(R.id.usd_sell);
        plnBuyText=findViewById(R.id.pln_buy);
        plnSellText=findViewById(R.id.pln_sell);

        String usdBuy=intent.getStringExtra("USD_buy");
        String usdSell=intent.getStringExtra("USD_sell");
        usdBuyText.setText(usdBuy);
        usdSellText.setText(usdSell);

        evrBuyText=findViewById(R.id.evr_buy);
        evrSellText=findViewById(R.id.evr_sell);
        String evrBuy=intent.getStringExtra("EVR_buy");
        String evrSell=intent.getStringExtra("EVR_sell");
        evrBuyText.setText(evrBuy);
        evrSellText.setText(evrSell);


        gbpBuyText=findViewById(R.id.gbp_buy);
        gbpSellText=findViewById(R.id.gbp_sell);
        String gbpBuy=intent.getStringExtra("GBP_buy");
        String gbpSell=intent.getStringExtra("GBP_sell");
        gbpBuyText.setText(gbpBuy);
        gbpSellText.setText(gbpSell);

        plnBuyText=findViewById(R.id.pln_buy);
        plnSellText=findViewById(R.id.pln_sell);
        String plnBuy=intent.getStringExtra("PLN_buy");
        String plnSell=intent.getStringExtra("PLN_sell");
        plnBuyText.setText(plnBuy);
        plnSellText.setText(plnSell);
    }
}
