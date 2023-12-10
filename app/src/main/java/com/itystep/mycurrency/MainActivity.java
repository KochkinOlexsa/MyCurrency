package com.itystep.mycurrency;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.itystep.mycurrency.network.Currency;
import com.itystep.mycurrency.network.NetworkService;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int UAH = 980;
    public List<Currency> needCurrencies = new ArrayList<>();
    private Currency currencyFrom = new Currency();
    private Currency currencyTo = new Currency();
    private String[] currencyList;

    private EditText editTextPrice;
    private EditText editTextResult;
    private EditText editTextSum;

    private Button convertButton;
    private Button logUotButton;
    private Button currensyInfoButton;
    private RadioButton radioBuy, radioSell, radioCross;
    private Spinner spinnerCurrencyFrom, spinnerCurrencyTo;
    private ImageView userFoto;
    private TextView greetingText;
    private Toolbar toolbar;
    DecimalFormat df = new DecimalFormat("#,###.##");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater menuInflater=getMenuInflater();
         menuInflater.inflate(R.menu.menu,menu);

        return true;
    }

    public MainActivity() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerCurrencyFrom = findViewById(R.id.currencyFrom);
        spinnerCurrencyTo = findViewById(R.id.currencyTo);
toolbar=findViewById(R.id.appbar);
setSupportActionBar(toolbar);
toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.logOut){
            FirebaseAuth.getInstance().signOut();
            Intent loginIntend = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntend);
        }
        return false;
    }
});
        Intent intent = getIntent();
        String userName = intent.getStringExtra("user_name");
        String photoURL = intent.getStringExtra("url_img");
        userFoto = findViewById(R.id.userPhoto);
        Picasso.get()
                .load(photoURL)
                .placeholder(R.drawable.user_icon)
                .into(userFoto);

        greetingText = findViewById(R.id.greeting_user);
        greetingText.setText("Hello, " + userName + " ! ");



        currencyList = getResources().getStringArray(R.array.currency_list);
        spinnerCurrencyFrom.setOnItemSelectedListener(this);
        spinnerCurrencyTo.setOnItemSelectedListener(this);

        radioBuy = findViewById(R.id.buy);
        radioSell = findViewById(R.id.sell);
        radioCross = findViewById(R.id.cross);

        convertButton = findViewById(R.id.convert);

        currensyInfoButton = findViewById(R.id.currency_info);

        radioBuy.setOnClickListener(this);
        radioSell.setOnClickListener(this);
        radioCross.setOnClickListener(this);
        convertButton.setOnClickListener(this);
        currensyInfoButton.setOnClickListener(this);

        List<Integer> currencyCodes = Arrays.asList(840, 978, 985, 826);

        editTextPrice = findViewById(R.id.price);
        editTextResult = findViewById(R.id.result);
        editTextSum = findViewById(R.id.sum);

        NetworkService.getInstance()
                .getMonoAPI()
                .getCurrencies()
                .enqueue(new Callback<List<Currency>>() {
                    @SuppressLint("SuspiciousIndentation")
                    @Override
                    public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                        List<Currency> currencies = response.body();
                        for (Currency currency : currencies) {
                            if (currencyCodes.contains(currency.getCodeA()) && currency.getCodeB() == UAH) {
                                switch (currency.getCodeA()) {
                                    case 840:
                                        currency.setCode("USD");

                                        break;
                                    case 826:
                                        currency.setCode("GBP");
                                        break;
                                    case 978:
                                        currency.setCode("EUR");
                                        break;
                                    case 985:
                                        currency.setCode("PLN");
                                        break;

                                }
                                needCurrencies.add(currency);
                            }

                        }
                        editTextPrice.setText("  / ");
                    }

                    @Override
                    public void onFailure(Call<List<Currency>> call, Throwable t) {
                        editTextPrice.setText("Error");
                    }
                });
    }

    public Currency getCurrencyByCode(String code) {
        Currency findCurrency = new Currency();
        for (Currency currency : needCurrencies)
            if (currency.getCode().equalsIgnoreCase(code)) {
                findCurrency = currency;

                break;
            }
        return findCurrency;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.buy) {
            if (currencyFrom.getRateBuy() == 0) {
                editTextPrice.setText("buy:" + currencyFrom.getRateCross());
            } else {
                editTextPrice.setText("buy:" + currencyFrom.getRateBuy());
            }

        }
        if (v.getId() == R.id.sell) {
            if (currencyFrom.getRateSell() == 0) {
                editTextPrice.setText("sell:" + currencyFrom.getRateCross());
            } else {
                editTextPrice.setText("sell:" + currencyFrom.getRateSell());
            }
        }
        if (v.getId() == R.id.cross) {
            editTextPrice.setText("cross:" + currencyFrom.getRateCross());
        }
        if (v.getId() == R.id.currency_info) {
            Intent currensyIntent = new Intent(getApplicationContext(), CurrenciesActivity.class);
            for (Currency currency : needCurrencies) {
                double buyPrice = currency.getRateBuy() != 0 ? currency.getRateBuy() : currency.getRateCross();
                currensyIntent.putExtra(currency.getCode() + "_buy", df.format(buyPrice));
                double sellPrice = currency.getRateSell() != 0 ? currency.getRateSell() : currency.getRateCross();
                currensyIntent.putExtra(currency.getCode() + "_sell", df.format(sellPrice));

            }
            startActivity(currensyIntent);
        }
        if (v.getId() == R.id.convert) {
            double result = 0;
            String currencyFromCode = spinnerCurrencyFrom.getSelectedItem().toString().substring(0, 3);
            String currencyToCode = spinnerCurrencyTo.getSelectedItem().toString().substring(0, 3);


            if (currencyFromCode.equalsIgnoreCase(currencyToCode)) {
                result = Double.parseDouble(editTextSum.getText().toString());
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Вибрано однакові типи валют.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 230);
                toast.show();
            } else if (currencyFromCode.equalsIgnoreCase("UAH")) {
                Double sum = Double.parseDouble(editTextSum.getText().toString());
                Double price = currencyTo.getRateSell() != 0 ? currencyTo.getRateSell() : currencyTo.getRateCross();
                result = sum / price;
            } else if (currencyToCode.equalsIgnoreCase("UAH")) {
                Double sum = Double.parseDouble(editTextSum.getText().toString());
                Double price = currencyFrom.getRateBuy() != 0 ? currencyFrom.getRateBuy() : currencyFrom.getRateCross();
                result = sum * price;
            } else {
                Double sum = Double.parseDouble(editTextSum.getText().toString());
                Double priceBuy = currencyFrom.getRateBuy() != 0 ? currencyFrom.getRateBuy() : currencyFrom.getRateCross();
                Double uah = sum * priceBuy;
                Double priceSell = currencyTo.getRateSell() != 0 ? currencyTo.getRateSell() : currencyTo.getRateCross();
                result = uah / priceSell;
            }

            editTextResult.setText(df.format(result) + " " + currencyToCode);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String code = spinnerCurrencyFrom.getSelectedItem().toString().substring(0, 3);
        currencyFrom = getCurrencyByCode(code);

        code = spinnerCurrencyTo.getSelectedItem().toString().substring(0, 3);
        currencyTo = getCurrencyByCode(code);

        editTextPrice.setText("From: " + currencyFrom.getRateSell()
                + "To: " + currencyTo.getRateSell());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}