public class Solicitacao {
 private int codigo;
 private String solicitante;
 private String descricao;
 private String categoria;
 private int prioridade;
 private String status;
 private String dataHoraAbertura;
 private String responsavel;

 public Solicitacao() {
  this.codigo = 0;
  this.solicitante = "";
  this.descricao = "";
  this.categoria = "";
  this.prioridade = 0;
  this.status = "";
  this.dataHoraAbertura = "";
  this.responsavel = "";
 }

 public Solicitacao(int codigo, String solicitante, String descricao, String categoria, int prioridade, String status, String dataHoraAbertura, String responsavel) {
  this.codigo = codigo;
  this.solicitante = solicitante;
  this.descricao = descricao;
  this.categoria = categoria;
  this.prioridade = prioridade;
  this.status = status;
  this.dataHoraAbertura = dataHoraAbertura;
  this.responsavel = responsavel;

 }
 public int getCodigo() {
  return codigo;
 }
 public void setCodigo(int codigo) {
  this.codigo = codigo;
 }
    public String getSolicitante() {
        return solicitante;
    }
    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public int getPrioridade() {
        return prioridade;
    }
    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDataHoraAbertura() {
        return dataHoraAbertura;
    }
    public void setDataHoraAbertura(String dataHoraAbertura) {
        this.dataHoraAbertura = dataHoraAbertura;
    }
    public String getResponsavel() {
        return responsavel;
    }
    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }
}