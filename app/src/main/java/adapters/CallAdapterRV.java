package adapters;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import br.com.ownard.forgetme.MainActivity;
import br.com.ownard.forgetme.R;
import models.Contato;

/**
 * Created by marcelo.cunha on 22/01/2018.
 */

public class CallAdapterRV extends RecyclerView.Adapter<CallAdapterRV.ContatoViewHolder> {

    private List<Contato> mContatos;
    private int layoutInflate;
    Activity currentActivity;

    public CallAdapterRV(List<Contato> mContatos, int layout, Activity activity) {
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
    public void onBindViewHolder(ContatoViewHolder holder, final int position) {


        holder.tvName.setText(mContatos.get(position).getName());
        holder.tvNumber.setText(mContatos.get(position).getPhoneNumber());
        holder.tvNumberCallBlock.setText(String.valueOf(mContatos.get(position).getBlockCalls()));

        if(holder.imgPhoto != null && mContatos.get(position).getPhoto() != null){
            holder.imgPhoto.setImageBitmap(mContatos.get(position).getPhoto());
        }

        holder.btnDeleteCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)currentActivity).getCallController().delete(mContatos.get(position).getPhoneNumber());
                Toast.makeText(currentActivity, mContatos.get(position).getName() + R.string.number_removed, Toast.LENGTH_SHORT).show();
                mContatos.remove(mContatos.get(position));

                ((MainActivity)currentActivity).notifyRVChange();
                ((MainActivity)currentActivity).notifyRVItemRemoved();
            }
        });

    }

    public void notifyData(List<Contato> contatos){
        this.mContatos = contatos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mContatos.size();
    }


    public static class ContatoViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName;
        public TextView tvNumber;
        public TextView isEmpty;
        public TextView tvNumberCallBlock;
        public ImageView imgPhoto;
        public ImageButton btnDeleteCall;

        public ContatoViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.tv_nome_contato);
            tvNumber = (TextView)itemView.findViewById(R.id.tv_number_contato);
            isEmpty = (TextView)itemView.findViewById(R.id.isEmpty);
            tvNumberCallBlock = (TextView)itemView.findViewById(R.id.tv_number_callblock);
            imgPhoto = (ImageView)itemView.findViewById(R.id.iv_contact);
            btnDeleteCall = (ImageButton)itemView.findViewById(R.id.ic_aux);

        }
    }
}
