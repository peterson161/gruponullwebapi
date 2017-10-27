package gruponullfiap.investepague;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText txtSenhaLogin,txtCPFLogin;
    TextView btnInvestidor, btnCliente, btnNovoCadastro;
    String cpfLogin, senhaLogin;
    int telaInicial;

    String urlConf;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");

        btnInvestidor = (TextView) findViewById(R.id.btnInvestidor);
        btnCliente = (TextView) findViewById(R.id.btnCliente);
        btnNovoCadastro = (TextView) findViewById(R.id.btnNovoCadastro);
        btnNovoCadastro.setTypeface(custom_font1);
        btnInvestidor.setTypeface(custom_font);
        btnCliente.setTypeface(custom_font);
        txtCPFLogin = (EditText) findViewById(R.id.txtCPFLogin);
        txtSenhaLogin = (EditText) findViewById(R.id.txtSenhaLogin);
        txtCPFLogin.setTypeface(custom_font);
        txtCPFLogin.addTextChangedListener(MaskUtil.insert(txtCPFLogin, MaskUtil.MaskType.CPF));
        txtSenhaLogin.setTypeface(custom_font);

        btnInvestidor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cpfLogin = txtCPFLogin.getText().toString();
                cpfLogin = cpfLogin.replace(".", "");
                cpfLogin = cpfLogin.replace("-", "");
                senhaLogin = txtSenhaLogin.getText().toString();
                efetuarLogin(1);
            }
        });

        btnCliente.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cpfLogin = txtCPFLogin.getText().toString();
                cpfLogin = cpfLogin.replace(".", "");
                cpfLogin = cpfLogin.replace("-", "");
                senhaLogin = txtSenhaLogin.getText().toString();
                efetuarLogin(2);
            }
        });

        btnNovoCadastro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String>{
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this, "Aguarde", "Efetuando login... ");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                //URL url = new URL("http://gruponullwebapi.azurewebsites.net/api/Pessoas?CPF=" + cpfLogin);
                Host host = new Host();
                URL url = new URL("http://" + host.getHost() + "/pessoas/" + cpfLogin);
                urlConf = url.toString();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if(connection.getResponseCode() == 200){
                    BufferedReader stream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String linha = "";
                    StringBuilder resposta = new StringBuilder();
                    while((linha = stream.readLine()) != null){
                        resposta.append(linha);
                    }

                    connection.disconnect();
                    return resposta.toString();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s != null){
               try{
                   JSONObject jsonObject = new JSONObject(s);
                   String senha = jsonObject.getString("senha");
                   if(senha.equals(senhaLogin)){
                       switch (telaInicial){
                           case 1: {
                               loginInvestidor(s);
                               break;
                           }
                           case 2: {
                               loginCliente(s);
                               break;
                           }
                       }
                   }else{
                        dialogOk("Login", "Senha incorreta");
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
           }else{
                dialogOk("Erro ao efetuar login", "CPF e/ou senha inválidos.\n" + urlConf);
           }
        }
    }

    private void dialogOk(String title, String message){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setMessage(message);

        b.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        b.show();
    }

    private void loginInvestidor(String s){
        Intent it = new Intent(LoginActivity.this, AreaInvestidorActivity.class);
        it.putExtra("InvestidorLogado", s);
        startActivity(it);
        finish();
    }

    private void loginCliente(String s){
        Intent it = new Intent(LoginActivity.this, AreaClienteActivity.class);
        it.putExtra("ClienteLogado", s);
        startActivity(it);
        finish();
    }

    private void efetuarLogin(int telaInicial){
        this.telaInicial = telaInicial;
        LoginTask loginTask = new LoginTask();
        loginTask.execute();
    }

}
