package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.CoresEnum;

public class PicColorAdapter extends RecyclerView.Adapter<PicColorAdapter.PicColorViewHolder> {

    private final List<CoresEnum> colors = new ArrayList<>();
    private final Context context;


    //CallBack clickEvent
    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public PicColorAdapter(Context context) {
        this.context = context;
        colors.addAll(Arrays.asList(CoresEnum.values()));
    }

    @NonNull
    @Override
    public PicColorAdapter.PicColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_pic_color, parent, false);
        return new PicColorViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PicColorAdapter.PicColorViewHolder holder, final int position) {
        holder.setColorsOnView(colors.get(position));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class PicColorViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public PicColorViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPicColor);
        }

        public void setColorsOnView(CoresEnum coresEnum) {
            // Log.i("COR", "" + coresEnum);
            imageView.setColorFilter(cor(coresEnum));
        }
    }

    public int cor(CoresEnum coresEnum) {

        switch (coresEnum) {

            case AZUL:
                return 0xFF408EC9;
            case BRANCO:
                return 0xFFFFFFFF;
            case VERMELHO:
                return 0xFFEC2F4B;
            case VERDE:
                return 0xFF9ACD32;
            case AMARELO:
                return 0xFFF9F256;
            case LILAS:
                return 0xFFF1CBFF;
            case CINZA:
                return 0xFFD2D4DC;
            case MARROM:
                return 0xFFA47C48;
            case ROXO:
                return 0xFFBE29EC;
            default:
                return 0xFFFFFFFF;
        }
    }


    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }


}
