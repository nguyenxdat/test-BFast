package vn.datnx.todolist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopeer.itemtouchhelperextension.Extension;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;

import butterknife.BindView;
import vn.datnx.todolist.R;
import vn.datnx.todolist.adapter.helper.ViewHolderHelper;
import vn.datnx.todolist.model.ItemTodo;

public class AdapterListTodo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ItemTodo> listTodo;
    private Context context;
    private LayoutInflater layoutInflater;
    private ItemTouchHelperExtension itemTouchHelperExtension;

    public AdapterListTodo(Context context, ArrayList<ItemTodo> listTodo) {
        this.context = context;
        this.listTodo = listTodo;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_todo, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemTodo itemTodo = listTodo.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        bindData(viewHolder, itemTodo, position);
    }

    private void bindData(final ViewHolder viewHolder, ItemTodo itemTodo, int position) {
        viewHolder.txtTitle.setText(itemTodo.getTitle());
        viewHolder.txtTitle.setTextColor(itemTodo.isComplete() ? Color.RED : Color.BLACK);
        viewHolder.itemView.setOnClickListener(onClickListener);
        viewHolder.imgEdit.setOnClickListener(onClickListener);
        viewHolder.imgDelete.setOnClickListener(onClickListener);
    }

    public void addAllObject(ArrayList<ItemTodo> listTodo) {
        this.listTodo = listTodo;
        notifyDataSetChanged();
    }

    public void addObject(ItemTodo itemTodo) {
        this.listTodo.add(itemTodo);
        notifyItemInserted(listTodo.size() - 1);
    }

    public void addObjectAtIndex(ItemTodo itemTodo, int index) {
        this.listTodo.add(index, itemTodo);
        notifyItemInserted(index);
    }

    public void clearData() {
        this.listTodo.clear();
        notifyDataSetChanged();
    }

    public void removeObjectAtIndex(int position) {
        this.listTodo.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void notifyItemAtIndex(int index, ItemTodo itemTodo) {
        this.listTodo.set(index, itemTodo);
        notifyItemChanged(index);
    }

    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        this.itemTouchHelperExtension = itemTouchHelperExtension;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemTouchHelperExtension.closeOpened();
        }
    };

    @Override
    public int getItemCount() {
        return listTodo.size();
    }

    public void move(int from, int to) {
        ItemTodo prev = listTodo.remove(from);
        listTodo.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    public ArrayList<ItemTodo> getListItem() {
        return listTodo;
    }

    public class ViewHolder extends ViewHolderHelper implements Extension {

        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.img_edit)
        ImageView imgEdit;
        @BindView(R.id.img_delete)
        ImageView imgDelete;
        @BindView(R.id.container_content)
        public RelativeLayout containerContent;
        @BindView(R.id.container_action_right)
        public LinearLayout containerActionRight;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return containerActionRight.getWidth();
        }
    }
}
