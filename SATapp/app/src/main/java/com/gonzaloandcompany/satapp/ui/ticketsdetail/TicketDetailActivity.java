package com.gonzaloandcompany.satapp.ui.ticketsdetail;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.data.viewmodel.AnotacionViewModel;
import com.gonzaloandcompany.satapp.data.viewmodel.UserViewModel;
import com.gonzaloandcompany.satapp.mymodels.Anotacion;
import com.gonzaloandcompany.satapp.mymodels.Asignacion;
import com.gonzaloandcompany.satapp.mymodels.Estado;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.mymodels.UsuarioDummy;
import com.gonzaloandcompany.satapp.requests.CreateAnotacion;
import com.gonzaloandcompany.satapp.requests.TicketAssignRequest;
import com.gonzaloandcompany.satapp.requests.TicketUpdateStateRequest;
import com.gonzaloandcompany.satapp.ui.ImagesSliderAdapter;
import com.gonzaloandcompany.satapp.ui.anotaciones.AnotacionDialogFragment;
import com.gonzaloandcompany.satapp.ui.tickets.TicketsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketDetailActivity extends AppCompatActivity{
    private Ticket ticket;
    private String idTicket;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private ImagesSliderAdapter adapter;
    private TicketsViewModel ticketsViewModel;
    private int positionImg = 0;
    private UserViewModel userViewModel;
    private UsuarioDummy currentUser;
    private List<UsuarioDummy> techs;
    private String techId = "";
    private List<UsuarioDummy> allTechs;
    private AnotacionViewModel anotacionViewModel;
    private List<String> anotacionList;
    public AnotationListener listener;

    @BindView(R.id.ticket_detail_edit_title)
    TextView editTitle;
    @BindView(R.id.ticketDetailAdd)
    FloatingActionButton add;
    @BindView(R.id.ticketDetailDelete)
    FloatingActionButton delete;
    @BindView(R.id.ticketDetailEdit)
    FloatingActionButton edit;
    @BindView(R.id.ticket_assign)
    FloatingActionButton assign;
    @BindView(R.id.ticket_detail_layot_tech)
    LinearLayout techLayout;
    @BindView(R.id.ticketDetailCreatedAt)
    TextView createdAt;
    @BindView(R.id.ticketDetailCreatedBy)
    TextView createdBy;
    @BindView(R.id.ticketDetailState)
    TextView state;
    @BindView(R.id.ticketDetailDescription)
    TextView description;
    @BindView(R.id.ticketDetailTech)
    ListView techAssigned;
    @BindView(R.id.lvAnotaciones)
    RecyclerView lvAnotaciones;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        ButterKnife.bind(this);
        ticketsViewModel = new ViewModelProvider(this).get(TicketsViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        anotacionViewModel = new ViewModelProvider(this).get(AnotacionViewModel.class);
        getCurrentUser();

    }

    public void getCurrentUser() {
        userViewModel.getCurrentUser().observe(this, new Observer<UsuarioDummy>() {
            @Override
            public void onChanged(UsuarioDummy usuario) {
                currentUser = usuario;
                getTicket();
                setButtons();
            }
        });
    }

    public void getTicket() {
        idTicket = getIntent().getStringExtra("ticketID");

        ticketsViewModel.getTicket(idTicket).observe(this, new Observer<Ticket>() {
            @Override
            public void onChanged(Ticket data) {
                if (data != null) {
                    ticket = data;
                    Log.d("TICKET LOADED",ticket.toString());
                    initComponent();
                    getTechs();
                    loadAnotaciones();

                    if (Estado.PENDIENTE_ASIGNACION.toString().equals(ticket.getEstado()))
                        state.setText(Estado.PENDIENTE_ASIGNACION.getDescription());
                    else if (Estado.ASIGNADA.toString().equals(ticket.getEstado()))
                        state.setText(Estado.ASIGNADA.getDescription());
                    else if (Estado.EN_PROCESO.toString().equals(ticket.getEstado()))
                        state.setText(Estado.EN_PROCESO.getDescription());
                    else if (Estado.SOLUCIONADA.toString().equals(ticket.getEstado()))
                        state.setText(Estado.SOLUCIONADA.getDescription());

                    state.setVisibility(View.VISIBLE);
                    description.setText(ticket.getDescripcion());
                    description.setVisibility(View.VISIBLE);

                    if (ticket.getCreado_por().getName() != null)
                        createdBy.setText(ticket.getCreado_por().getName());
                    else
                        createdBy.setText(ticket.getCreado_por().getEmail());
                    createdBy.setVisibility(View.VISIBLE);

                    LocalDate date = LocalDate.parse(ticket.getFecha_creacion().substring(0, 10));
                    createdAt.setText(date.toString("dd/MM/yyyy"));
                    createdAt.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    public void loadAnotaciones(){
        anotacionViewModel.getAnotacionesByTicket(idTicket).observe(this, new Observer<List<Anotacion>>() {
            @Override
            public void onChanged(List<Anotacion> anotaciones) {
                if(!anotaciones.isEmpty()){
                    Collections.sort(anotaciones);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(TicketDetailActivity.this);
                    lvAnotaciones.setLayoutManager(layoutManager);
                    AnotationRecyclerAdapter adapterAnotation= new AnotationRecyclerAdapter(anotaciones,TicketDetailActivity.this);
                    lvAnotaciones.setAdapter(adapterAnotation);

                    lvAnotaciones.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getTechs() {
        userViewModel.getUsers().observe(this, new Observer<List<UsuarioDummy>>() {
            @Override
            public void onChanged(List<UsuarioDummy> usuarios) {
                if (usuarios != null || !usuarios.isEmpty()) {
                    listTechs(usuarios);
                    setAssigns();
                    setTechButton();
                }
            }
        });
    }

    public void listTechs(List<UsuarioDummy> usuarios) {
        allTechs = usuarios.stream().filter(x -> x.getRole().equals("tecnico")).collect(Collectors.toList());

        techs = new ArrayList<>();
        techs.addAll(allTechs);

        for (Asignacion a : ticket.getAsignaciones()) {
            for (UsuarioDummy u : new ArrayList<UsuarioDummy>(techs)) {
                if (a.getTecnico_id().equals(u.getId())) {
                    techs.remove(u);
                }
            }
        }
    }

    public void setAssigns() {
        List<String> assigned = new ArrayList<>();
        for (UsuarioDummy u : allTechs) {
            for (Asignacion s : ticket.getAsignaciones()) {
                if (s.getTecnico_id().equals(u.getId())) {

                    if (u.getName() != null)
                        assigned.add(u.getName());
                    else
                        assigned.add(u.getEmail());
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                TicketDetailActivity.this,
                android.R.layout.simple_list_item_1,
                assigned) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextAppearance(android.R.style.TextAppearance_Small);
                return view;
            }
        };

        techAssigned.setAdapter(adapter);

        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, techAssigned);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = techAssigned.getLayoutParams();
            params.height = totalHeight + (techAssigned.getDividerHeight() * (adapter.getCount() - 1));
            techAssigned.setLayoutParams(params);
            techAssigned.requestLayout();
        }

        techAssigned.setVisibility(View.VISIBLE);
    }

    public void setTechButton() {
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (techs.isEmpty()) {
                    Toast.makeText(TicketDetailActivity.this, "No hay técnicos disponibles", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(TicketDetailActivity.this);
                    dialog.setTitle("Seleccione un técnico");
                    String[] namesTechs = new String[techs.size()];
                    for (int i = 0; i < namesTechs.length; i++) {
                        namesTechs[i] = techs.get(i).getName();
                    }
                    dialog.setItems(namesTechs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            techId = techs.get(which).getId();
                            TicketAssignRequest request = new TicketAssignRequest(techId);
                            ticketsViewModel.assignTech(ticket.getId(), request).observe(TicketDetailActivity.this, new Observer<Ticket>() {
                                @Override
                                public void onChanged(Ticket ticket2) {
                                    if (ticket.getEstado().equals("ASIGNADA"))
                                        getCurrentUser();
                                    else {
                                        TicketUpdateStateRequest request1 = new TicketUpdateStateRequest("ASIGNADA");
                                        ticketsViewModel.updateState(ticket2.getId(), request1).observe(TicketDetailActivity.this, new Observer<Ticket>() {
                                            @Override
                                            public void onChanged(Ticket ticket) {
                                                getCurrentUser();
                                            }
                                        });
                                    }

                                }

                            });
                        }
                    });

                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                }
            }
        });
    }

    public void setButtons() {
        if (currentUser.getRole().equals("user")) {
            techLayout.setVisibility(View.GONE);
        } else {
            techLayout.setVisibility(View.VISIBLE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new AnotacionDialogFragment(idTicket, null);
                dialog.show(getSupportFragmentManager(), "AnotacionDialogFragment");
            }
        });

        if (!currentUser.getRole().equals("user")) editTitle.setText("Editar estado");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentUser.getRole().equals("user")) {
                    List<String> statesNames = new ArrayList<>();
                    for (Estado e : Estado.values()) {
                        statesNames.add(e.getDescription());
                    }

                    AlertDialog.Builder dialog = new AlertDialog.Builder(TicketDetailActivity.this);
                    dialog.setTitle("Seleccione el estado de la incidencia");

                    String[] namesStates = new String[statesNames.size()];
                    for (int i = 0; i < namesStates.length; i++) {
                        namesStates[i] = statesNames.get(i);
                    }

                    dialog.setItems(namesStates, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String currentState = Estado.getEstadoByDescription(statesNames.get(which)).getName();
                            TicketUpdateStateRequest request = new TicketUpdateStateRequest(currentState);
                            ticketsViewModel.updateState(ticket.getId(), request).observe(TicketDetailActivity.this, new Observer<Ticket>() {
                                @Override
                                public void onChanged(Ticket ticket2) {
                                    getCurrentUser();
                                }

                            });
                        }
                    });

                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = dialog.create();
                    alert.show();
                } else {
                    FragmentManager fm = getFragmentManager();
                    DialogFragment dialogFragment = new TicketEditDialog(ticket.getId(), ticketsViewModel, TicketDetailActivity.this);
                    dialogFragment.show(getSupportFragmentManager(), "TicketDetailDialog");

                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TicketDetailActivity.this);
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ticketsViewModel.deleteTicket(ticket.getId());
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.setMessage("¿Estás seguro de que quieres eliminar el ticket " + ticket.getTitulo());
                builder.setTitle(R.string.app_name);
                builder.show();

            }
        });

    }


    private void initComponent() {
        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new ImagesSliderAdapter(this, ticket.getFotos());
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapter.getCount(), 0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                positionImg = pos;
                addBottomDots(layout_dots, adapter.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(ContextCompat.getColor(this, R.color.overlay_dark_10), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        }
    }


}
