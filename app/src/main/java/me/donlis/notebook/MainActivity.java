package me.donlis.notebook;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.donlis.notebook.bean.Note;
import me.donlis.notebook.database.greenDao.db.DaoSession;
import me.donlis.notebook.database.greenDao.db.NoteDao;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private BaseApplication app;//自定义application
    private List<Note> dataList = new ArrayList<>();//列表的数据
    private DaoSession daoSession;//
    private ListAdapter listAdapter;//列表适配器

    @BindView(R.id.add)
    FloatingActionButton button_add;//新增按钮

    @BindView(R.id.listView)
    ListView listView;//列表控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //使用ButterKnife,初始化对控件的引用
        ButterKnife.bind(this);

        initView();

        loadData();
    }

    /**
     * 初始化数据和UI的绑定事件
     */
    private void initView(){
        app = (BaseApplication) getApplication();
        daoSession = app.getDaoSession();

        button_add.setOnClickListener(this);

        listAdapter = new ListAdapter(this, dataList);
        listView.setOnItemClickListener(this);
        listView.setAdapter(listAdapter);
    }

    /**
     * 加载数据并刷新listView
     */
    private void loadData(){
        //创建Note的QueryBuilder对象
        QueryBuilder<Note> queryBuilder = daoSession.queryBuilder(Note.class);
        //添加查询条件，条件:时间降序排序
        QueryBuilder<Note> noteQueryBuilder = queryBuilder.orderDesc(NoteDao.Properties.DateTime);
        //列出查询结果
        List<Note> list = noteQueryBuilder.list();

        if(list == null){
            list = new ArrayList<>();
        }
        dataList.clear();
        dataList.addAll(list);

        //刷新数据
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.add://新增按钮点击事件
                Intent addIntent = new Intent(this,NoteEditActivity.class);
                startActivityForResult(addIntent,RequestCode.MAIN_REQUEST);
                break;
        }
    }

    /**
     * 列表项点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent addIntent = new Intent(this,NoteEditActivity.class);
        Note curNote = dataList.get(position);
        addIntent.putExtra("id",curNote.getId());
        startActivityForResult(addIntent,RequestCode.MAIN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //判断requestCode和resultCode等于对应code
        if(requestCode == RequestCode.MAIN_REQUEST && resultCode == RequestCode.MAIN_RESULT){
            if(data == null){
                return;
            }
            boolean isRefresh = data.getBooleanExtra("isRefresh", false);
            //isRefresh等于true时才需要刷新
            if(isRefresh){
                loadData();
            }
        }
    }
}
