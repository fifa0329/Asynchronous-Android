package com.packt.asyncandroid.chapter5.example1;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.packt.asyncandroid.LaunchActivity;

import java.math.BigInteger;

public class PendingIntentPrimesIntentService extends IntentService {

    public static final String PARAM = "prime_to_find";
    public static final String PENDING_RESULT = "pending_result";
    public static final String RESULT = "result";
    public static final int RESULT_CODE = "nth_prime".hashCode();
    public static final int INVALID = "invalid".hashCode();

    public PendingIntentPrimesIntentService() {
        super("primes");

        Logger.d("是否是主线程=" + (Looper.myLooper() == Looper.getMainLooper()));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.d("是否是主线程=" + (Looper.myLooper() == Looper.getMainLooper()) + "Thread=" + Thread.currentThread().getName());
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT);
        int n = intent.getIntExtra(PARAM, -1);
        try {
            if (n < 2) {
                reply.send(INVALID);
            } else {
                BigInteger prime = new BigInteger("2");
                Intent result = new Intent();
                for (int i = 0; i < n; i++) {
                    prime = prime.nextProbablePrime();
                    result.putExtra(RESULT, prime);
                    reply.send(this, RESULT_CODE, result);
                }
            }
        } catch (PendingIntent.CanceledException exc) {
            Log.i(LaunchActivity.TAG, "reply cancelled", exc);
        }
    }
}
