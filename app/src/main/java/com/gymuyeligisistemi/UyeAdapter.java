package com.gymuyeligisistemi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Üye ListView Adapter
 * Üye listesini ListView'da göstermek için kullanılan özel adapter
 */
public class UyeAdapter extends BaseAdapter {

    private Context context;
    private List<Uye> uyeListesi;
    private LayoutInflater inflater;

    public UyeAdapter(Context context, List<Uye> uyeListesi) {
        this.context = context;
        this.uyeListesi = uyeListesi;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return uyeListesi.size();
    }

    @Override
    public Object getItem(int position) {
        return uyeListesi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return uyeListesi.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_uye, parent, false);
            holder = new ViewHolder();
            holder.txtAdSoyad = convertView.findViewById(R.id.txtItemAdSoyad);
            holder.txtTelefon = convertView.findViewById(R.id.txtItemTelefon);
            holder.txtUyelikTipi = convertView.findViewById(R.id.txtItemUyelikTipi);
            holder.txtSiraNo = convertView.findViewById(R.id.txtItemSiraNo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Uye uye = uyeListesi.get(position);

        holder.txtSiraNo.setText(String.valueOf(uye.getId()));
        holder.txtAdSoyad.setText(uye.getAd() + " " + uye.getSoyad());
        holder.txtTelefon.setText(uye.getTelefon());
        holder.txtUyelikTipi.setText(uye.getUyelikTipi());

        return convertView;
    }

    // ViewHolder pattern - performans için
    private static class ViewHolder {
        TextView txtSiraNo;
        TextView txtAdSoyad;
        TextView txtTelefon;
        TextView txtUyelikTipi;
    }
}
