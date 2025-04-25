package com.example.sports.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sports.R;
import com.example.sports.model.Player;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<Player> players;
    private OnPlayerRemoveListener removeListener;

    public interface OnPlayerRemoveListener {
        void onRemove(Player player);
    }

    public PlayerAdapter(List<Player> players, OnPlayerRemoveListener removeListener) {
        this.players = players;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.bind(player);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView playerNameTextView;
        private ImageButton deletePlayerButton;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerNameTextView);
            deletePlayerButton = itemView.findViewById(R.id.deletePlayerButton);
        }

        public void bind(Player player) {
            playerNameTextView.setText(player.getName());
            deletePlayerButton.setOnClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onRemove(player);
                }
            });
        }
    }
}