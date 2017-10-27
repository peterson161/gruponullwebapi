package gruponullfiap.investepague;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class CadastroActivity extends AppCompatActivity {
    private EditText txtNome, txtCPF, txtRG,
            txtCEP, txtEndereco, txtBairro, txtCidade, txtEmail,
            txtDDD1, txtTelefone1, txtDDD2, txtTelefone2,
            txtSenha, txtSenhaConfirmacao;
    private Spinner spnUF;
    private TextView btnCadastrar, btnCancelar;

    private String conteudoJason = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        txtNome = (EditText) findViewById(R.id.txtNome);
        txtCPF = (EditText) findViewById(R.id.txtCPF);
        txtCPF.addTextChangedListener(MaskUtil.insert(txtCPF, MaskUtil.MaskType.CPF));
        txtRG = (EditText) findViewById(R.id.txtRG);
        txtCEP = (EditText) findViewById(R.id.txtCEP);
        txtCEP.addTextChangedListener(MaskUtil.insert(txtCEP, MaskUtil.MaskType.CEP));
        txtEndereco = (EditText) findViewById(R.id.txtEndereco);
        txtBairro = (EditText) findViewById(R.id.txtBairro);
        txtCidade = (EditText) findViewById(R.id.txtCidade);
        txtEmail = (EditText) findViewById(R.id.txtEndereco);
        txtDDD1 = (EditText) findViewById(R.id.txtDDD1);
        txtDDD1.addTextChangedListener(MaskUtil.insert(txtDDD1, MaskUtil.MaskType.DDD));
        txtTelefone1 = (EditText) findViewById(R.id.txtTelefone1);
        txtTelefone1.addTextChangedListener(MaskUtil.insert(txtTelefone1, MaskUtil.MaskType.TelefoneC));
        txtDDD2 = (EditText) findViewById(R.id.txtDDD2);
        txtDDD2.addTextChangedListener(MaskUtil.insert(txtDDD2, MaskUtil.MaskType.DDD));
        txtTelefone2 = (EditText) findViewById(R.id.txtTelefone2);
        txtTelefone2.addTextChangedListener(MaskUtil.insert(txtTelefone2, MaskUtil.MaskType.TelefoneC));
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        txtSenhaConfirmacao = (EditText) findViewById(R.id.txtSenhaConfirmacao);
        spnUF = (Spinner) findViewById(R.id.spnUF);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.listaUF, R.layout.spinner_item);
        //spnUF.setAdapter(adapter);
        btnCadastrar = (TextView) findViewById(R.id.btnCadastrar);
        btnCancelar = (TextView) findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validaSenha(txtSenha.getText().toString(), txtSenhaConfirmacao.getText().toString())){
                    runTask();
                }else{
                    finalizar("Senhas diferentes","A senha e a confirmação devem ser iguais.");
                    //Toast.makeText(CadastroActivity.this, "Senhas diferentes. A senha e sua confirmação devem ser iguais", Toast.LENGTH_SHORT).show();
                    //runTask();
                }
            }
        });

    }

    private void runTask(){
        String cpf, telefone1, telefone2, cep;
        cpf = txtCPF.getText().toString();
        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        telefone1 = txtTelefone1.getText().toString();
        telefone1 = telefone1.replace(".", "");
        telefone1 = telefone1.replace("-", "");
        telefone2 = txtTelefone2.getText().toString();
        telefone2 = telefone2.replace(".", "");
        telefone2 = telefone2.replace("-", "");
        cep = txtCEP.getText().toString();
        cep = cep.replace("-", "");

        CadastraPessoaTask cadastraPessoaTask = new CadastraPessoaTask();
        cadastraPessoaTask.execute(
                "22",
                txtNome.getText().toString(),
                cpf,
                txtRG.getText().toString(),
                cep,
                txtEndereco.getText().toString(),
                txtBairro.getText().toString(),
                txtCidade.getText().toString(),
                spnUF.getSelectedItem().toString(),
                txtEmail.getText().toString(),
                txtDDD1.getText().toString(),
                telefone1,
                txtDDD2.getText().toString(),
                telefone2,
                txtSenha.getText().toString()
        );
    }

    private boolean validaSenha(String senha, String senhaConfirmacao){
        if(senha.equals(senhaConfirmacao))
            return true;
        else
            return false;
    }

    private void finalizar(String title, String message){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setMessage(message);

        b.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                openLogin();
            }
        });

        b.show();
    }

    private void openLogin(){
        Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(it);
        finish();
    }

    private class CadastraPessoaTask extends AsyncTask<String, Void, Integer>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CadastroActivity.this, "Aguarde", "Gravando dados...");
        }

        @Override
        protected Integer doInBackground(String... params) {

            URL url = null;
            try{
                //url = new URL("http://gruponullwebapi.azurewebsites.net/api/Pessoas/");
                Host host = new Host();
                url = new URL("http://" + host.getHost() + "/pessoas/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONStringer json = new JSONStringer();
                json.object();
                json.key("id_pessoa").value(params[0]);
                json.key("nome").value(params[1]);
                json.key("cpf").value(params[2]);
                json.key("rg").value(params[3]);
                json.key("cep").value(params[4]);
                json.key("endereco").value(params[5]);
                json.key("bairro").value(params[6]);
                json.key("cidade").value(params[7]);
                json.key("uf").value(params[8]);
                json.key("email").value(params[9]);
                json.key("ddd1").value(params[10]);
                json.key("telefone1").value(params[11]);
                json.key("ddd2").value(params[12]);
                json.key("telefone2").value(params[13]);
                json.key("senha").value(params[14]);
                json.endObject();

                OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
                streamWriter.write(json.toString());

                conteudoJason = json.toString();

                streamWriter.close();

                return connection.getResponseCode();

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer i) {
            Toast.makeText(CadastroActivity.this, conteudoJason, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            if(i == 201){
                finalizar("Cadastro efetuado com sucesso", "Efetue login com seu CPF e senha.");
            }else{
                finalizar("Erro ao efetuar o cadastro", "Código: " + i + "\n\n" + conteudoJason);
            }
        }
    }

}
