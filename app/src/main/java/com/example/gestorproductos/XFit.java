package com.example.gestorproductos;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gestorproductos.databinding.FragmentXFitBinding;

import java.util.ArrayList;

public class XFit extends Fragment {

    private FragmentXFitBinding binding;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private ProductDialog productDialog;
    private ActivityResultLauncher<String> seleccionarImagen;
    private static final int TIENDA_ID = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // El launcher DEBE registrarse aquí, en onCreate
        seleccionarImagen = registerForActivityResult(
                new ActivityResultContracts.GetContent(), uri -> {
                    if (uri != null && productDialog != null) {
                        requireContext().getContentResolver()
                                .takePersistableUriPermission(uri,
                                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        productDialog.setImagenUri(uri);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentXFitBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        productDialog = new ProductDialog(requireContext(), dbHelper, this::cargarProductos);

        adapter = new ProductAdapter(new ArrayList<>(), new ProductAdapter.OnProductClickListener() {
            @Override
            public void onEditClick(Product product) {
                productDialog.mostrarDialogEditar(product,
                        () -> seleccionarImagen.launch("image/*"));
            }

            @Override
            public void onDeleteClick(Product product) {
                mostrarDialogEliminar(product);
            }
        });

        binding.rvProductos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProductos.setAdapter(adapter);

        binding.fabAgregar.setOnClickListener(v ->
                productDialog.mostrarDialogAgregar(TIENDA_ID,
                        () -> seleccionarImagen.launch("image/*")));

        cargarProductos();
    }

    private void mostrarDialogEliminar(Product product) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar producto")
                .setMessage("¿Estás seguro de que deseas eliminar " + product.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    dbHelper.deleteProducto(product.getId());
                    cargarProductos();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cargarProductos() {
        adapter.actualizarLista(dbHelper.getProductosByTienda(TIENDA_ID));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}