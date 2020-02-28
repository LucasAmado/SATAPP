package com.gonzaloandcompany.satapp.ui.ticketsdetail;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.gonzaloandcompany.satapp.R;
import com.gonzaloandcompany.satapp.mymodels.Estado;
import com.gonzaloandcompany.satapp.mymodels.Ticket;
import com.gonzaloandcompany.satapp.ui.tickets.TicketsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.LocalDate;

import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketDetailActivity extends AppCompatActivity {
    private Ticket ticket;
    private String idTicket;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private TicketImagesAdapter adapter;
    private TicketsViewModel ticketsViewModel;
    private int positionImg = 0;

    @BindView(R.id.ticketDetailAdd)
    FloatingActionButton add;
    @BindView(R.id.ticketDetailDelete)
    FloatingActionButton delete;
    @BindView(R.id.ticketDetailEdit)
    FloatingActionButton edit;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        ButterKnife.bind(this);
        ticketsViewModel = new ViewModelProvider(this).get(TicketsViewModel.class);

        //TODO: ESCONDER SEGÚN EL ROL DEL USUARIO
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getTicket();
    }

    public void getTicket() {
        idTicket = getIntent().getStringExtra("ticketID");

        ticketsViewModel.getTicket(idTicket).observe(this, new Observer<Ticket>() {
            @Override
            public void onChanged(Ticket data) {
                if (data != null) {
                    ticket = data;

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

                    //TODO: PEDIR A LUISMI QUE LA API RETORNE EL NOMBRE DEL USUARIO QUE CREÓ EL TICKET.
                    createdBy.setText(ticket.getCreado_por().getName());
                    createdBy.setVisibility(View.VISIBLE);

                    LocalDate date = LocalDate.parse(ticket.getFecha_creacion().substring(0, 9));
                    createdAt.setText(date.toString("dd/MM/yyyy"));
                    createdAt.setVisibility(View.VISIBLE);


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            TicketDetailActivity.this,
                            android.R.layout.simple_list_item_1,
                            ticket.getAsignaciones().stream().map(x -> x.getTecnico_id()).collect(Collectors.toList())

                    ) {
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

                    ListAdapter listadp = techAssigned.getAdapter();
                    if (listadp != null) {
                        int totalHeight = 0;
                        for (int i = 0; i < listadp.getCount(); i++) {
                            View listItem = listadp.getView(i, null, techAssigned);
                            listItem.measure(0, 0);
                            totalHeight += listItem.getMeasuredHeight();
                        }
                        ViewGroup.LayoutParams params = techAssigned.getLayoutParams();
                        params.height = totalHeight + (techAssigned.getDividerHeight() * (listadp.getCount() - 1));
                        techAssigned.setLayoutParams(params);
                        techAssigned.requestLayout();
                    }
                    techAssigned.setVisibility(View.VISIBLE);
                    initComponent();
                }
            }
        });
    }

    private void initComponent() {

        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new TicketImagesAdapter(this, ticket.getFotos());
        viewPager.setAdapter(adapter);

        // displaying selected image first
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