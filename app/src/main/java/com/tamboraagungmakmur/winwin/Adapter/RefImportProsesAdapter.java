package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefArea;
import com.tamboraagungmakmur.winwin.Model.RefProsesImport;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefImportProsesAdapter extends RecyclerView.Adapter<RefImportProsesAdapter.EmailTempViewHolder> {

    private ArrayList<RefProsesImport> dataArrayList;
    private Context context;

    public RefImportProsesAdapter(ArrayList<RefProsesImport> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_proses_import, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefProsesImport value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.noId.setText(value.getId());
        holder.importDesk.setText(value.getDeskripsi());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, RefBankEditActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("id", value.getId());
//                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class EmailTempViewHolder extends RecyclerView.ViewHolder {

        private TextView no, noId, importDesk;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            noId = (TextView) itemView.findViewById(R.id.noId);
            importDesk = (TextView) itemView.findViewById(R.id.deskripsi);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
