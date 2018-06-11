package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.ownard.forgetme.MainActivity;
import br.com.ownard.forgetme.R;
import models.Contato;

/**
 * Created by marcelo.cunha on 22/01/2018.
 */

public class ContactAdapterRV extends RecyclerView.Adapter<ContactAdapterRV.ContatoViewHolder> {

    private List<Contato> mContatos;
    private int layoutInflate;
    private Activity currentActivity;

    public ContactAdapterRV(List<Contato> mContatos, int layout, Activity activity) {
        this.mContatos = mContatos;
        this.layoutInflate = layout;
        this.currentActivity = activity;
    }

    @Override
    public ContatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutInflate, parent, false);
        ContatoViewHolder contatoVH = new ContatoViewHolder(v);
        return contatoVH;
    }

    @Override
    public void onBindViewHolder(final ContatoViewHolder holder, final int position) {
        holder.tvName.setText(mContatos.get(position).getName());
        holder.tvNumber.setText(mContatos.get(position).getPhoneNumber());
        final ImageButton btnAddBlock = (ImageButton)holder.btnAddBlock;

        btnAddBlock.setBackgroundResource(R.drawable.ic_add_black_36dp);

        if(holder.imgPhoto != null && mContatos.get(position).getPhoto() != null){
            holder.imgPhoto.setImageBitmap(mContatos.get(position).getPhoto());
        }

        if (((MainActivity)currentActivity).getCallController().checkPhone(mContatos.get(position).getPhoneNumber())){
            btnAddBlock.setBackgroundResource(R.drawable.ic_block_black_36dp);
        }

        holder.btnAddBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!((MainActivity)currentActivity).getCallController().checkPhone(mContatos.get(position).getPhoneNumber())){
                    ((MainActivity) currentActivity).getCallController().insert(mContatos.get(position).getName(),mContatos.get(position).getPhoneNumber());
                    Toast.makeText(currentActivity, R.string.add_blocked, Toast.LENGTH_SHORT).show();
                    btnAddBlock.setBackgroundResource(R.drawable.ic_block_black_36dp);
                    ((MainActivity)currentActivity).notifyRVChange();
                }else{
                    Toast.makeText(currentActivity, R.string.already_add, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void notifyData(List<Contato> contatos){
        this.mContatos = contatos;
        notifyDataSetChanged();
    }


    public void addListItem(Contato c, int position){
        mContatos.add(c);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return mContatos.size();
    }



    public static class ContatoViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName;
        public TextView tvNumber;
        public ImageView imgPhoto;
        public ImageButton btnAddBlock;

        public ContatoViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.tv_nome_contato);
            tvNumber = (TextView)itemView.findViewById(R.id.tv_number_contato);
            imgPhoto = (ImageView)itemView.findViewById(R.id.iv_contact);
            btnAddBlock = (ImageButton)itemView.findViewById(R.id.btn_contact_addblock);

        }
    }
}
