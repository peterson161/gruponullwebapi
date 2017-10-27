package gruponullfiap.investepague;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class MinhasSolicitacoesActivity extends AppCompatActivity {
    TextView btnSolicitarNovoInvestimento, btnVoltarInvestimento;
    ListView lstSolicitacoes;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_solicitacoes);

        btnSolicitarNovoInvestimento = (TextView) findViewById(R.id.btnSolicitarNovoInvestimento);
        btnVoltarInvestimento = (TextView) findViewById(R.id.btnVoltarInvestimentos);
        lstSolicitacoes = (ListView) findViewById(R.id.lstSolicitacoes);

        Bundle extras = getIntent().getExtras();
        try{
            jsonObject = new JSONObject(extras.getString("InvestidorLogado"));
        }catch(JSONException e){
            e.printStackTrace();
        }

        btnSolicitarNovoInvestimento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(MinhasSolicitacoesActivity.this, SolicitacaoInvestimentoActivity.class);
                startActivity(it);
            }
        });

        btnVoltarInvestimento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(MinhasSolicitacoesActivity.this, AreaInvestidorActivity.class);
                it.putExtra("InvestidorLogado", jsonObject.toString());
                startActivity(it);
                finish();
            }
        });

        ListaItemTask listaItemTask = new ListaItemTask();
        listaItemTask.execute();
    }

    private void dialogOk(String title, String message){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setMessage(message);

        b.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        b.show();
    }

    private class ListaItemTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MinhasSolicitacoesActivity.this, "Aguarde", "Recuperando solicitações...");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Host host = new Host();
                URL url = new URL("http://" + host.getHost() + "/minhasSolicitacoes/" + jsonObject.getInt("id_pessoa"));
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

            if (s != null){
                try{
                    List<MinhasSolicitacoes> listaSolicitacoes = new ArrayList<MinhasSolicitacoes>();

                    JSONObject jsonobj = new JSONObject(s.toString());

                    //JSONArray jsonArray = new JSONArray(s.toString());//jsonobj.getJSONArray("minhasSolicitacoes");

                    //for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject item = jsonobj;
                        int idInvestidor = item.getInt("investidor_id_investidor");
                        int idSolicitacao = item.getInt("id_solicitacao");
                        String dataCriacao = item.getString("data_criacao");
                        double valor = item.getDouble("valor");
                        int mesesDuracao = item.getInt("meses_duracao");
                        double percJuros = item.getDouble("perc_juros_mes");
                        MinhasSolicitacoes minhasSolicitacoes =  new MinhasSolicitacoes(idInvestidor, idSolicitacao, dataCriacao, valor, mesesDuracao, percJuros);
                        listaSolicitacoes.add(minhasSolicitacoes);
                        ListAdapter adapter = new ArrayAdapter<MinhasSolicitacoes>(MinhasSolicitacoesActivity.this,
                                android.R.layout.simple_list_item_1, listaSolicitacoes);
                        lstSolicitacoes.setAdapter(adapter);

                    //}
                }catch(JSONException e){
                    Toast.makeText(MinhasSolicitacoesActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    dialogOk("", e.toString());
                }
            }else{
                Toast.makeText(MinhasSolicitacoesActivity.this, "Ocorreu um erro.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
