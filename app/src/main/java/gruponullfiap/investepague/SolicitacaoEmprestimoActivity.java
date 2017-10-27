package gruponullfiap.investepague;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SolicitacaoEmprestimoActivity extends AppCompatActivity {

    private TextView btnSolicitarEmprestimo, btnCancelarSolicitacao;
    private LinearLayout layoutDados;

    int id_titulo;
    double valor;
    int meses_duracao;
    double perc_juros_mes;
    int id_cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_emprestimo);
        layoutDados = (LinearLayout) findViewById(R.id.ll2);

        btnSolicitarEmprestimo = (TextView) findViewById(R.id.btnSolicitarEmprestimo);
        btnCancelarSolicitacao = (TextView) findViewById(R.id.btnCancelarSolicitacao);

        btnSolicitarEmprestimo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Intent it = new Intent(SolicitacaoEmprestimoActivity.this, AreaClienteActivity.class);
                //startActivity(it);
                confirmarEmprestimo();
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

    }

    private void dialogOkCancel(String title, String message){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setMessage(message);

        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

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

    private void confirmarEmprestimo(){
        //dialogOkCancel("Solicitação de empréstimo", "Confirma solicitação de empréstimo ?");
        SolicitacaoTask solicitacaoTask = new SolicitacaoTask();
        solicitacaoTask.execute(
                Integer.toString(id_titulo),
                Double.toString(valor),
                Integer.toString(meses_duracao),
                Double.toString(perc_juros_mes),
                Integer.toString(id_cliente)
        );
    }

    private class ListaItemTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SolicitacaoEmprestimoActivity.this, "Aguarde...", "Recuperando pacotes para investimento...");
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                Host host = new Host();
                URL url = new URL("http://" + host.getHost() + "/emprestimosDisponiveis");
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

            EmprestimosDisponiveis emprestimosDisponiveis;
            if (s != null){
                try{
                    JSONArray jsonArray = new JSONArray(s.toString());

                    final RadioButton[] rb = new RadioButton[jsonArray.length()];
                    RadioGroup rg = new RadioGroup(SolicitacaoEmprestimoActivity.this); //create the RadioGroup
                    rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject item = (JSONObject) jsonArray.get(i);
                        int idSolicitacao = item.getInt("id_solicitacao");
                        double valor = item.getDouble("valor");
                        int mesesDuracao = item.getInt("meses_duracao");
                        double percJuros = item.getDouble("perc_juros_mes");
                        int idInvestidor = item.getInt("investidor_id_investidor");
                        String parcelas = item.getString("parcelas");
                        emprestimosDisponiveis =  new EmprestimosDisponiveis(idSolicitacao, valor, mesesDuracao, percJuros, idInvestidor, parcelas);

                        rb[i]  = new RadioButton(SolicitacaoEmprestimoActivity.this);
                        rb[i].setText(" " + emprestimosDisponiveis.getParcelas());
                        rb[i].setId(idSolicitacao);
                        rg.addView(rb[i]);
                    }

                    rg.check(rb[0].getId());

                    layoutDados.addView(rg);//you add the whole RadioGroup to the layout

                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(SolicitacaoEmprestimoActivity.this, "Ocorreu um erro.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SolicitacaoTask extends AsyncTask<String, Void, Integer>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SolicitacaoEmprestimoActivity.this, "Aguarde", "Gravando dados...");
        }

        @Override
        protected Integer doInBackground(String... params) {

            URL url = null;
            try{
                //url = new URL("http://gruponullwebapi.azurewebsites.net/api/Pessoas/");
                Host host = new Host();
                url = new URL("http://" + host.getHost() + "/solicitacaoEmprestimo/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONStringer json = new JSONStringer();
                json.object();
                json.key("id_titulo").value(params[0]);
                json.key("valor").value(params[1]);
                json.key("meses_duracao").value(params[2]);
                json.key("perc_juros_mes").value(params[3]);
                json.key("id_cliente").value(params[4]);
                json.endObject();

                OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
                streamWriter.write(json.toString());
                streamWriter.close();

                return connection.getResponseCode();

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer i) {
            progressDialog.dismiss();
            if(i == 201){
                dialogOk("Cadastro efetuado com sucesso", "Efetue login com seu CPF e senha.");
            }else{
                dialogOk("Erro ao efetuar o cadastro", "Código: " + i);
            }
        }
    }

}
