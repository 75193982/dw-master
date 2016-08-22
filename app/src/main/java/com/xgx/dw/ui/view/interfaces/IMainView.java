package com.xgx.dw.ui.view.interfaces;

import com.xgx.dw.base.IBaseView;
import com.xgx.dw.vo.response.UpdateVersionResult;

public abstract interface IMainView
  extends IBaseView
{
  public abstract void checkVersionCallBack(UpdateVersionResult paramUpdateVersionResult);
  
  public abstract void switchAddressBook();
  
  public abstract void switchAlpha(int paramInt);
  
  public abstract void switchFind();
  
  public abstract void switchMe();
  
  public abstract void switchWX();
}


