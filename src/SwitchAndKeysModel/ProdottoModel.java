package SwitchAndKeysModel;

import java.sql.SQLException;
import java.util.Collection;

public interface ProdottoModel {
  boolean doDelete(int paramInt) throws SQLException;
  
  ProdottoBean doRetrieveByKey(int paramInt) throws SQLException;
  
  Collection<ProdottoBean> doRetrieveAll(String paramString) throws SQLException;
  
  boolean doSave(ProdottoBean paramProdottoBean, String paramString) throws SQLException;
  
  boolean doModify(ProdottoBean paramProdottoBean, int paramInt, String paramString) throws SQLException;

  boolean aggiornaQuantita(int idProdotto, int quantitaAcquistata) throws SQLException;
}

