package gruponullfiap.investepague;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AreaInvestidorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lstItens;
    TextView lblNomeInvestidor;
    JSONObject jsonObject;
    String nomeInvestidor, cpfLogin, idPessoaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_investidor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        lblNomeInvestidor = (TextView)header.findViewById(R.id.lblNomeInvestidor);

        lstItens = (ListView) findViewById(R.id.lstItens1);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            //
        } else {
            try {
                jsonObject = new JSONObject(extras.getString("InvestidorLogado"));
                nomeInvestidor = jsonObject.getString("nome");
                cpfLogin = jsonObject.getString("cpf");
                lblNomeInvestidor.setText(nomeInvestidor);
                idPessoaLogin = jsonObject.getString("idpessoa");
            }catch(Exception e){
                e.printStackTrace();
            }

        }

        Toast.makeText(this, "Olá, " + nomeInvestidor, Toast.LENGTH_SHORT).show();
        ListaItemTask listaItemTask = new ListaItemTask();
        listaItemTask.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.area_cliente, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //     return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent it = null;
        if (id == R.id.nav_SolicitarInv){
            it = new Intent(this, SolicitacaoInvestimentoActivity.class);
            it.putExtra("InvestidorLogado", jsonObject.toString());
            startActivity(it);
        } else if (id == R.id.nav_MeusInvestimentos) {
           // it = new Intent(this, MeusInvestimentosActivity.class);
            //it.putExtra("InvestidorLogado", jsonObject.toString());
            //startActivity(it);
            //finish();
        } else if (id == R.id.nav_MinhasSolicitacoes) {
            it = new Intent(this, MinhasSolicitacoesActivity.class);
            it.putExtra("InvestidorLogado", jsonObject.toString());
            startActivity(it);
            finish();
        } else if (id == R.id.nav_Logoff) {
            it = new Intent(this, LoginActivity.class);

            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Logoff");
            b.setMessage("Deseja efetuar logoff ?");

            b.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AreaInvestidorActivity.this.finish();
                    Intent it = new Intent(AreaInvestidorActivity.this, LoginActivity.class);
                    startActivity(it);
                }
            });

            b.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            b.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ListaItemTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AreaInvestidorActivity.this, "Aguarde...", "Consultando as cotações do dia.");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Host host = new Host();
                URL url = new URL("http://api.promasters.net.br/cotacao/v1/valores");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if(connection.getResponseCode() == 200){
                    BufferedReader stream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String linha;
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

            if (s != null){
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    String valores = jsonObject.getString("valores");
                    JSONObject jsonObjectMoedas = new JSONObject(valores);
                    List<Cotacao> listaCotacoes = new ArrayList<Cotacao>();
                    String usd = jsonObjectMoedas.getString("USD");
                    JSONObject jsonObjectUSD = new JSONObject(usd);
                    String eur = jsonObjectMoedas.getString("EUR");
                    JSONObject jsonObjectEUR = new JSONObject(eur);
                    String ars = jsonObjectMoedas.getString("ARS");
                    JSONObject jsonObjectARS = new JSONObject(ars);
                    String gbp = jsonObjectMoedas.getString("GBP");
                    JSONObject jsonObjectGBP = new JSONObject(gbp);
                    String btc = jsonObjectMoedas.getString("BTC");
                    JSONObject jsonObjectBTC = new JSONObject(btc);

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(jsonObjectUSD);
                    jsonArray.put(jsonObjectEUR);
                    jsonArray.put(jsonObjectARS);
                    jsonArray.put(jsonObjectGBP);
                    jsonArray.put(jsonObjectBTC);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String nome = obj.getString("nome");
                        String valor = obj.getString("valor");
                        String fonte = obj.getString("fonte");
                        Cotacao cotacao = new Cotacao(nome, valor, fonte);
                        listaCotacoes.add(cotacao);
                    }
                    ListAdapter adapter = new ArrayAdapter<Cotacao>(AreaInvestidorActivity.this,
                                android.R.layout.simple_list_item_1, listaCotacoes);
                    lstItens.setAdapter(adapter);
                }catch(Exception e){
                    e.printStackTrace();
                    dialogoOk("Erro", e.getMessage().toString());
                }
            }else{
                Toast.makeText(AreaInvestidorActivity.this, "Ocorreu um erro.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void dialogoOk(String title, String message){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setMessage(message);

        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        b.show();
    }
}
