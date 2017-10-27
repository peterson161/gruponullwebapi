package gruponullfiap.investepague;

/**
 * Created by peterson161 on 10/24/17.
 */

public class Cotacao {
    private String nome;
    private String valor;
    private String fonte;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public Cotacao(String nome, String valor, String fonte) {
        this.nome = nome;
        this.valor = valor;
        this.fonte = fonte;
    }

    @Override
    public String toString() {
        return nome + "\nR$ " + valor + "\nFonte: " + fonte;
    }
}
