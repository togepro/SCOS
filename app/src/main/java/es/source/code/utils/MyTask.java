package es.source.code.utils;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MyTask extends AsyncTask <Void,Integer,Void>{
    private ProgressBar mProgressBar;
    private Context mContext;
    private String money;

    public MyTask(Context pContext, ProgressBar pProgressBar,String money) {

        mContext = pContext;
        mProgressBar = pProgressBar;
        this.money = money;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressBar.setProgress(0);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.i("tag","mytask");
        int _Mac = mProgressBar.getMax();
        int _Current = mProgressBar.getProgress();

        while (_Current <= _Mac) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            publishProgress(_Current + _Mac / 100);
            _Current = _Current + _Mac / 100;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mProgressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //mProgressBar.setProgress(0);
        Toast.makeText(mContext,"本次结账金额为："+money,Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
