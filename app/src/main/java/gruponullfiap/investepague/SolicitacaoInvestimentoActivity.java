package gruponullfiap.investepague;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import junit.runner.Version;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoInvestimentoActivity extends AppCompatActivity {

    private TextView btnSolicitarEmprestimo, btnCancelarSolicitacao;
    private LinearLayout layoutDados;
    private String nomeLogin;
    JSONObject jsonObject;

    int idCheck;
    int idPessoa;
    JSONArray jsonArrayPacotes;
    JSONObject pacoteSelecionado;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_investimento);

        btnSolicitarEmprestimo = (TextView) findViewById(R.id.btnSolicitarInvestimento);
        btnCancelarSolicitacao = (TextView) findViewById(R.id.btnCancelarSolicitacaoInv);

        Bundle extras = getIntent().getExtras();
        try{
            jsonObject = new JSONObject(extras.getString("InvestidorLogado"));
            idPessoa = jsonObject.getInt("id_pessoa");
        }catch(JSONException e){
            e.printStackTrace();
        }

        layoutDados = (LinearLayout) findViewById(R.id.ll);

        btnSolicitarEmprestimo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder b = new AlertDialog.Builder(SolicitacaoInvestimentoActivity.this);
                b.setTitle("Confirmação");
                b.setMessage("Confirma solicitação de empréstimo ?");

                b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        solicitaInvestimento();
                    }
                });

                b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                b.show();
            }
        });

        btnCancelarSolicitacao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Intent it = new Intent(SolicitacaoEmprestimoActivity.this, AreaClienteActivity.class);
                //startActivity(it);
                finish();
            }
        });


        ListaItemTask listaItemTask = new ListaItemTask();
        listaItemTask.execute();

    }

    private void solicitaInvestimento(){
        idCheck = rg.getCheckedRadioButtonId();
        try {
            pacoteSelecionado = (JSONObject) jsonArrayPacotes.getJSONObject(idCheck);
        }catch(Exception e){
            dialogOk("Erro", e.getMessage().toString());
        }

        Date data = new Date(System.currentTimeMillis());
        SimpleDateFormat formatarDate = new SimpleDateFormat("yyyy-MM-dd");
        SolicitaInvestimentoTask solicitaInvestimentoTask = new SolicitaInvestimentoTask();
        solicitaInvestimentoTask.execute(
                    formatarDate.format(data).toString(),
                    "2020-12-31",
                    Integer.toString(idCheck),
                    Integer.toString(idPessoa),
                    "SOL01"
                );
    }

    private void dialogOkCancel(String title, String message){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setMessage(message);

        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogOk("Investimento registrado", "Aguarde email com informações sobre clientes interessados.");
            }
        });

        b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        b.show();
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
            progressDialog = ProgressDialog.show(SolicitacaoInvestimentoActivity.this, "Aguarde...", "Recuperando pacotes para investimento...");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Host host = new Host();
                URL url = new URL("http://" + host.getHost() + "/configPacoteTitulo");
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

            ConfigPacoteTitulo pacoteTitulo;
            if (s != null){
                try{
                    jsonArrayPacotes = new JSONArray(s.toString());

                    final RadioButton[] rb = new RadioButton[jsonArrayPacotes.length()];
                    rg = new RadioGroup(SolicitacaoInvestimentoActivity.this); //create the RadioGroup
                    rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
                    for(int i = 0; i < jsonArrayPacotes.length(); i++){
                        JSONObject item = (JSONObject) jsonArrayPacotes.get(i);
                        int idPacoteTitulo = item.getInt("id_pacote_titulo");
                        String descricaoPacote = item.getString("descricao_pacote");
                        double valor = item.getDouble("valor");
                        int mesesDuracao = item.getInt("meses_duracao");
                        double percJuros = item.getDouble("perc_juros_mes");
                        pacoteTitulo =  new ConfigPacoteTitulo(idPacoteTitulo, descricaoPacote, valor, mesesDuracao, percJuros);

                        rb[i]  = new RadioButton(SolicitacaoInvestimentoActivity.this);
                        rb[i].setText(" " + pacoteTitulo.getDescricaoPacote());
                        rb[i].setId(idPacoteTitulo);
                        rg.addView(rb[i]);
                    }

                    rg.check(rb[0].getId());
                    layoutDados.addView(rg);//you add the whole RadioGroup to the layout

                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(SolicitacaoInvestimentoActivity.this, "Ocorreu um erro.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SolicitaInvestimentoTask extends AsyncTask<String, Void, Integer>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SolicitacaoInvestimentoActivity.this, "Aguarde", "Registrando solicitação...");
        }

        @Override
        protected Integer doInBackground(String... params) {
            try{
                //url = new URL("http://gruponullwebapi.azurewebsites.net/api/Pessoas/");
                Host host = new Host();
                URL url = new URL("http://" + host.getHost() + "/solicitacaoInvestimento");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONStringer json = new JSONStringer();
                json.object();
                json.key("data_criacao").value(params[0]);
                json.key("data_limite").value(params[1]);
                //json.key("data_cancelamento").value(params[2]);
                json.key("conf_pac_tit_id_pacote_titulo").value(params[2]);
                json.key("investidor_id_investidor").value(params[3]);
                json.key("status_sistema_id_status").value(params[4]);
                json.endObject();

                OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
                streamWriter.write(json.toString());
                streamWriter.close();

                return connection.getResponseCode();

            }catch(Exception e){
                e.printStackTrace();
                dialogOk("Erro", e.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer i) {
            progressDialog.dismiss();
            if(i == 201){
                dialogOk("Solicitação efetuada.", "Agora é só aguardar clientes interessados.");
            }else{
                dialogOk("Erro", "Código: " + i + "\n\n");
            }
        }
    }
}
