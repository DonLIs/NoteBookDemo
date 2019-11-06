package me.donlis.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.donlis.notebook.bean.Note;
import me.donlis.notebook.database.greenDao.db.DaoSession;
import me.donlis.notebook.database.greenDao.db.NoteDao;

public class NoteEditActivity extends AppCompatActivity {

    private BaseApplication app;

    private Date dateTime;//时间

    private Long fid;//数据Note在数据库对应的编码

    private DaoSession daoSession;

    private Menu mMenu;//菜单对象

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private boolean isInit = false;//是否在初始数据

    @BindView(R.id.date)
    TextView date_selector;//时间显示文本

    @BindView(R.id.title)
    EditText title_txt;//标题输入框

    @BindView(R.id.content)
    EditText content_txt;//多行文本输入框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        ButterKnife.bind(this);

        //显示返回箭头
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();
    }

    /**
     * 在创建菜单前初始化自定义的菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //初始化自定义的菜单
        getMenuInflater().inflate(R.menu.menu,menu);
        //记录菜单对象
        mMenu = menu;
        return true;
    }

    /**
     * 设置菜单中，保存按钮的显示或隐藏
     * @param visible
     */
    private void setSaveItemVisible(boolean visible){
        //在页面加载中，可能还没加载完菜单，需要判空
        if(mMenu == null){
            return;
        }
        if(mMenu.getItem(0) != null){
            mMenu.getItem(0).setVisible(visible);
        }
    }

    /**
     * 初始化数据和绑定事件
     */
    private void initView(){
        app = (BaseApplication) getApplication();
        daoSession = app.getDaoSession();

        //添加自定义的输入监听事件，监听有内容时才显示保存按钮，否则隐藏
        content_txt.addTextChangedListener(new DxTextWatcher() {
            @Override
            void onTextChanged(String s) {
                if(isInit){
                    return;
                }
                if(s.length() > 0){
                    setSaveItemVisible(true);
                }else{
                    setSaveItemVisible(false);
                }
            }
        });

        //从上个一页面获取id值
        //如果id为空，则是新增数据
        //如果id有值，则是记录id作为修改的id
        Intent intent = getIntent();
        fid = (Long) intent.getSerializableExtra("id");

        initForm();
    }

    /**
     * 初始化表单数据
     */
    private void initForm(){
        isInit = true;

        //判断有没有id
        if(fid != null){
            //创建QueryBuilder
            QueryBuilder<Note> noteQueryBuilder = daoSession.queryBuilder(Note.class);
            //查询id
            List<Note> list = noteQueryBuilder.where(NoteDao.Properties.Id.eq(fid)).list();
            if(list != null){
                Note note = list.get(0);
                title_txt.setText(note.getTitle());
                title_txt.clearFocus();
                content_txt.setText(note.getContent());
                content_txt.clearFocus();
                try{
                    dateTime = df.parse(note.getDateTime());
                }catch (ParseException e){
                    dateTime = new Date();
                }
                String format = df.format(dateTime);

                date_selector.setText(format);
            }
        }else{
            dateTime = new Date();

            String format = df.format(dateTime);

            date_selector.setText(format);
        }

        isInit = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://返回箭头点击事件
                forResult();
                finish();
                return true;
            case R.id.action_save://保存按钮点击事件
                Note note = new Note();
                String _title = title_txt.getText().toString();
                note.setTitle(_title);
                String _content = content_txt.getText().toString();
                note.setContent(_content);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateTime = new Date();
                String _date = format.format(dateTime);
                note.setDateTime(_date);

                //id不为空，则是修改
                //id为空，则是新增保存
                if(fid != null){
                    note.setId(fid);
                }
                //daoSession提供的新增或替换方法，并返回新增数量
                long insert = daoSession.insertOrReplace(note);
                if (insert > 0) {
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    setSaveItemVisible(false);
                    hideSoftInput();
                    initForm();
                } else {
                    Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 按回退键
     */
    @Override
    public void onBackPressed() {
        forResult();
        super.onBackPressed();
    }

    /**
     * 设置页面返回值
     */
    private void forResult(){
        Intent intent = new Intent();
        intent.putExtra("isRefresh",true);
        setResult(RequestCode.MAIN_RESULT,intent);
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}
