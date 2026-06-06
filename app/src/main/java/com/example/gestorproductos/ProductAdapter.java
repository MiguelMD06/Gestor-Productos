package com.example.gestorproductos;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gestorproductos.databinding.ItemProductoBinding;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemProductoBinding binding;

        public ProductViewHolder(ItemProductoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductoBinding binding = ItemProductoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder viewHolder, int position) {
        Product product = productList.get(position);
        Context context = viewHolder.binding.getRoot().getContext();

        // Badge de tienda
        if (product.getTienda_id() == 1) {
            viewHolder.binding.tvTiendaBadge.setText("XFit");
            viewHolder.binding.tvTiendaBadge.setTextColor(
                    context.getColor(R.color.badge_xfit_texto));
            viewHolder.binding.tvTiendaBadge.getBackground().setTint(
                    context.getColor(R.color.badge_xfit_fondo));
        } else {
            viewHolder.binding.tvTiendaBadge.setText("SportHouse");
            viewHolder.binding.tvTiendaBadge.setTextColor(
                    context.getColor(R.color.badge_sport_texto));
            viewHolder.binding.tvTiendaBadge.getBackground().setTint(
                    context.getColor(R.color.badge_sport_fondo));
        }

        // Nombre y precio
        viewHolder.binding.tvNombre.setText(product.getNombre());
        viewHolder.binding.tvPrecio.setText(
                String.format("$ %,.0f", product.getPrecio()));

        // Imagen
        String imagenRuta = product.getImagen();
        if (imagenRuta != null && !imagenRuta.isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(imagenRuta))
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(viewHolder.binding.ivProducto);
        } else {
            viewHolder.binding.ivProducto.setImageResource(R.drawable.ic_image_placeholder);
        }

        // Botones
        viewHolder.binding.btnEditar.setOnClickListener(v -> listener.onEditClick(product));
        viewHolder.binding.btnEliminar.setOnClickListener(v -> listener.onDeleteClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Para actualizar la lista desde el fragment
    public void actualizarLista(List<Product> nuevaLista) {
        this.productList = nuevaLista;
        notifyDataSetChanged();
    }
}