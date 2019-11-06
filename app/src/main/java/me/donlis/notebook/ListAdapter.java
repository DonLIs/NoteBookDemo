package me.donlis.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import me.donlis.notebook.bean.Note;

public class ListAdapter extends BaseAdapter {

    private Context mContext;

    private List<Note> notes;

    public ListAdapter(Context context,List<Note> notes){
        this.mContext = context;
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes == null ? 0 : notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item,null);
            holder = new ViewHolder();

            holder.date = convertView.findViewById(R.id.date);
            holder.title = convertView.findViewById(R.id.title);
            holder.content = convertView.findViewById(R.id.content);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = (Note) getItem(position);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String _date = "";
        try{
            _date = df.format(df.parse(note.getDateTime()));
        }catch (ParseException e){

        }
        holder.date.setText(_date);

        holder.title.setText(note.getTitle());

        holder.content.setText(note.getContent());

        return convertView;
    }

    /**
     * 复用对象
     */
    class ViewHolder{
        TextView date;
        TextView title;
        TextView content;
    }
}
