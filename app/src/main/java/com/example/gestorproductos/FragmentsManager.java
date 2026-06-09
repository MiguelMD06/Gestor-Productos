package com.example.gestorproductos;

import android.app.AlertDialog;
import android.os.Binder;
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
import androidx.viewbinding.ViewBinding;

import com.example.gestorproductos.databinding.FragmentSportHouseBinding;
import com.example.gestorproductos.databinding.FragmentXFitBinding;

import java.util.ArrayList;

public class XFit extends Fragment {

    private ViewBinding binding;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private ProductDialog productDialog;
    private ActivityResultLauncher<String> seleccionarImagen;
    private int tienda_id;
    private static final String ARG_TIENDA_ID = "tienda_id";

    public static XFit newInstance(int tiendaId) {
        XFit fragment = new XFit();
        Bundle args = new Bundle();
        args.putInt(ARG_TIENDA_ID, tiendaId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            tienda_id = getArguments().getInt(ARG_TIENDA_ID);

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
        if (tienda_id == 1){
            binding = FragmentXFitBinding.inflate(inflater,container,false);
            return ((FragmentXFitBinding) binding).getRoot();
        }else if(tienda_id == 2){
            binding = FragmentSportHouseBinding.inflate(inflater,container,false);
            return ((FragmentSportHouseBinding) binding).getRoot();
        }
        return null;
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

        if (binding instanceof FragmentXFitBinding xFit) {
            xFit.rvProductos.setAdapter(adapter);

            xFit.fabAgregar.setOnClickListener(v ->
                    productDialog.mostrarDialogAgregar(tienda_id,
                            () -> seleccionarImagen.launch("image/*")));
        }
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
        adapter.actualizarLista(dbHelper.getProductosByTienda(tienda_id));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}