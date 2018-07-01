package am.project.x.activities.develop.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import am.project.x.R;
import am.project.x.widgets.display.DisplayRecyclerView;
import am.util.mvp.AMAppCompatActivity;

public class TestActivity extends AMAppCompatActivity implements View.OnClickListener {

    private final DisplayAdapter mAdapter = new DisplayAdapter();
    private DisplayRecyclerView mVContent;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TestActivity.class));
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initializeActivity(@Nullable Bundle savedInstanceState) {
        setSupportActionBar(R.id.test_toolbar);

        mVContent = findViewById(R.id.display_rv_content);
        mVContent.setAdapter(mAdapter);

        findViewById(R.id.display_btn_temp).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.display_btn_temp:
                mVContent.setPagingEnable(!mVContent.isPagingEnable());
                break;
        }
    }
}
