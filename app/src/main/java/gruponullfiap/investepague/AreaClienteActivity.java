package gruponullfiap.investepague;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AreaClienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lstItens;
    TextView lblNomeCliente;
    JSONObject jsonObject;
    String nomeCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_cliente);
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
        lblNomeCliente = (TextView)header.findViewById(R.id.lblNomeCliente);
        lstItens = (ListView) findViewById(R.id.lstItens);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            //
        } else {
            try {
                jsonObject = new JSONObject(extras.getString("ClienteLogado"));
                nomeCliente = jsonObject.getString("Nome");
                lblNomeCliente.setText(nomeCliente);
            }catch(Exception e){
                e.printStackTrace();
            }

        }

        Toast.makeText(this, "Olá, " + nomeCliente, Toast.LENGTH_SHORT).show();

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
        if (id == R.id.nav_Solicitar) {
            it = new Intent(this, SolicitacaoEmprestimoActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_Confirmar) {
            it = new Intent(this, ConfirmacaoEmprestimoActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_MeusEmprestimos) {
            it = new Intent(this, AreaClienteActivity.class);
            finish();
            startActivity(it);
        } else if (id == R.id.nav_Logoff) {
            it = new Intent(this, LoginActivity.class);

            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Logoff");
            b.setMessage("Deseja efetuar logoff ?");

            b.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AreaClienteActivity.this.finish();
                    Intent it = new Intent(AreaClienteActivity.this, LoginActivity.class);
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
            progressDialog = ProgressDialog.show(AreaClienteActivity.this, "Aguarde...", "Recuperando empréstimos.");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = new URL("http://gruponullwebapi.azurewebsites.net/api/titulos/");
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
            /*
            if (s != null){
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("investimentos");

                    List<Titulo> listaTitulos = new ArrayList<Titulo>();

                    for(int i = 0; i <=jsonArray.length(); i++){
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        int idTitulo = item.getInt("idTitulo");
                        String dataEmissao = item.getString("dataEmissao");
                        double valorTitulo = item.getDouble("valorTitulo");
                        String dataVencimento = item.getString("dataVencimento");
                        int mesesDuracao = item.getInt("mesesDuracao");
                        double percJuros = item.getDouble("percJuros");
                        int idCategoria = item.getInt("idCategoria");
                        int idTituloOriginal = item.getInt("idTituloOriginal");
                        int idPacoteTitulo = item.getInt("idPacoteTitulo");
                        int idCliente = item.getInt("idCliente");
                        int idInvestidor = item.getInt("idInvestidor");
                        int idStatus = item.getInt("idStatus");

                        listaTitulos.add(new Titulo(idTitulo, dataEmissao, valorTitulo,
                                dataVencimento, mesesDuracao, percJuros, idCategoria,
                                idTituloOriginal, idPacoteTitulo, idCliente, idInvestidor, idStatus));

                    }
                    ListAdapter adapter = new ArrayAdapter<Titulo>(AreaInvestidorActivity.this,
                            android.R.layout.simple_list_item_1, listaTitulos);
                    lstItens.setAdapter(adapter);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(AreaInvestidorActivity.this, "Ocorreu um erro.", Toast.LENGTH_SHORT).show();
            }
            */

            String jsonProv = "{\"idTitulo\": \"1\",\"dataEmissao\": \"22/10/2017\",\"valorTitulo\": \"3000\",\"mesesDuracao\": \"12\",\"percJuros\": \"6\", \"idInvestidor\": \"1\"}";
            try {
                //JSONObject jsonObject = new JSONObject(jsonProv);
                List<Titulo> listaTitulos = new ArrayList<Titulo>();
                Date dataAtual = new Date();
                int idTitulo = 1;
                String dataEmissao = "22/10/2017";
                double valorTitulo = 2000.00;
                String dataVencimento = "23/10/2017";
                int mesesDuracao = 12;
                double percJuros = 6.00;
                int idInvestidor = 1;

                listaTitulos.add(new Titulo(idTitulo, dataEmissao, valorTitulo,
                        dataVencimento, mesesDuracao, percJuros, idInvestidor));

                ListAdapter adapter = new ArrayAdapter<Titulo>(AreaClienteActivity.this,
                        android.R.layout.simple_list_item_1, listaTitulos);
                lstItens.setAdapter(adapter);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
