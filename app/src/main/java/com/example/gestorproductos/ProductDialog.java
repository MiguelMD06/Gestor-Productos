package com.example.gestorproductos;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gestorproductos.databinding.DialogProductoBinding;

public class ProductDialog {

    public interface OnProductSavedListener {
        void onProductSaved();
    }

    private final Context context;
    private final DatabaseHelper dbHelper;
    private final OnProductSavedListener listener;
    private DialogProductoBinding binding;
    private Uri imagenUri = null;

    public ProductDialog(Context context, DatabaseHelper dbHelper, OnProductSavedListener listener) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    public void setImagenUri(Uri uri) {
        this.imagenUri = uri;
        if (binding != null && uri != null) {
            binding.ivPreviewImagen.setVisibility(View.VISIBLE);
            binding.ivIconoImagen.setVisibility(View.GONE);
            binding.tvHintImagen.setVisibility(View.GONE);
            Glide.with(context)
                    .load(uri)
                    .centerCrop()
                    .into(binding.ivPreviewImagen);
        }
    }

    public void mostrarDialogAgregar(int tiendaId, Runnable onAbrirGaleria) {
        binding = DialogProductoBinding.inflate(LayoutInflater.from(context));
        imagenUri = null;

        binding.tvDialogTitulo.setText("Agregar producto");

        binding.llSelectorImagen.setOnClickListener(v -> onAbrirGaleria.run());

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (validarYGuardar(tiendaId, dialog)) {
                    listener.onProductSaved();
                }
            });
        });

        dialog.show();
    }

    public void mostrarDialogEditar(Product product, Runnable onAbrirGaleria) {
        binding = DialogProductoBinding.inflate(LayoutInflater.from(context));

        binding.tvDialogTitulo.setText("Editar producto");
        binding.etNombre.setText(product.getNombre());
        binding.etPrecio.setText(String.valueOf(product.getPrecio()));

        // Cargar imagen existente si tiene
        if (product.getImagen() != null && !product.getImagen().isEmpty()) {
            imagenUri = Uri.parse(product.getImagen());
            binding.ivPreviewImagen.setVisibility(View.VISIBLE);
            binding.ivIconoImagen.setVisibility(View.GONE);
            binding.tvHintImagen.setVisibility(View.GONE);
            Glide.with(context)
                    .load(imagenUri)
                    .centerCrop()
                    .into(binding.ivPreviewImagen);
        } else {
            imagenUri = null;
        }

        binding.llSelectorImagen.setOnClickListener(v -> onAbrirGaleria.run());

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (validarYActualizar(product.getId(), dialog)) {
                    listener.onProductSaved();
                }
            });
        });

        dialog.show();
    }

    private boolean validarYGuardar(int tiendaId, AlertDialog dialog) {
        String nombre = binding.etNombre.getText().toString().trim();
        String precioStr = binding.etPrecio.getText().toString().trim();

        if (nombre.isEmpty()) {
            binding.etNombre.setError("Ingresa el nombre del producto");
            return false;
        }
        if (precioStr.isEmpty()) {
            binding.etPrecio.setError("Ingresa el precio del producto");
            return false;
        }

        double precio = Double.parseDouble(precioStr);
        String imagenRuta = imagenUri != null ? imagenUri.toString() : "";

        boolean exito = dbHelper.insertProducto(nombre, precio, imagenRuta, tiendaId);
        if (exito) {
            Toast.makeText(context, "Producto agregado", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return true;
        } else {
            Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validarYActualizar(int id, AlertDialog dialog) {
        String nombre = binding.etNombre.getText().toString().trim();
        String precioStr = binding.etPrecio.getText().toString().trim();

        if (nombre.isEmpty()) {
            binding.etNombre.setError("Ingresa el nombre del producto");
            return false;
        }
        if (precioStr.isEmpty()) {
            binding.etPrecio.setError("Ingresa el precio del producto");
            return false;
        }

        double precio = Double.parseDouble(precioStr);
        String imagenRuta = imagenUri != null ? imagenUri.toString() : "";

        boolean exito = dbHelper.updateProducto(id, nombre, precio, imagenRuta);
        if (exito) {
            Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return true;
        } else {
            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}