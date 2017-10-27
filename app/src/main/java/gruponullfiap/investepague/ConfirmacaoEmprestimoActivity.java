package gruponullfiap.investepague;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConfirmacaoEmprestimoActivity extends AppCompatActivity {

    private TextView btnConfirmarContratacao, btnCancelarContratacao, btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao_emprestimo);

        btnConfirmarContratacao = (TextView) findViewById(R.id.btnConfirmarContratacao);
        btnCancelarContratacao = (TextView) findViewById(R.id.btnCancelarContratacao);
        btnCancelar = (TextView) findViewById(R.id.btnCancelar);

        btnConfirmarContratacao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Intent it = new Intent(SolicitacaoEmprestimoActivity.this, AreaClienteActivity.class);
                //startActivity(it);
                confirmarEmprestimo();
            }
        });

        btnCancelarContratacao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Intent it = new Intent(SolicitacaoEmprestimoActivity.this, AreaClienteActivity.class);
                //startActivity(it);
                cancelarConfirmacao();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener()
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
                dialogOk("Contratação de empréstimo", "Parabéns. Empréstimo confirmado.");
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
        dialogOkCancel("Contratação de empréstimo", "Deseja contratar o empréstimo selecionado ?");
    }

    private void cancelarConfirmacao(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Cancelar");
        b.setMessage("Deseja cancelar esta solicitação de empréstimo definitivamente ?");

        b.setPositiveButton("Efetuar cancelamento", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogOk("Cancelamento", "Solicitação de empréstimo excluída definitivamente.");
            }
        });

        b.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        b.show();
    }

}