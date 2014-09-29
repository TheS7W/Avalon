package me.aventium.avalon.team.commands;

import me.aventium.avalon.Avalon;
import me.aventium.avalon.Command;
import me.aventium.avalon.team.Team;
import me.aventium.avalon.team.TeamCommand;
import me.aventium.avalon.team.listeners.Damage;
import me.aventium.avalon.team.listeners.WarpMove;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Command(name = "hq", aliases = {}, permission = "", description = "Teleport to your team's HQ")
public class HQ extends TeamCommand {

    @Override
    public boolean managerCommand() {
        return false;
    }

    @Override
    public void syncExecute(Player player, String[] args) {
        Team team = Avalon.get().getTeamManager().getPlayerTeam(player);
        if(team == null) {
            player.sendMessage("§cYou are not on a team!");
            return;
        }

        if(team.getHQ() == null) {
            player.sendMessage("§cYour team does not have an HQ set!");
            return;
        }

        boolean insta = true;

        for(Entity ent : player.getNearbyEntities(20, 256, 20)) {
            if(ent instanceof Player && Avalon.get().getTeamManager().getPlayerTeam((Player) ent) != team) {
                insta = false;
                return;
            }
        }


        if(insta) {
            player.teleport(team.getHQ());
            player.sendMessage("§7You cannot attack for 10 seconds.");
            Damage.addPlayer(player.getUniqueId());
        } else {
            player.sendMessage("§7There are players nearby! You must wait 10 seconds to warp.");
            new WarpMove(player, team.getHQ());
        }
    }

    @Override
    public void asyncExecute(Player player, String[] args) {

    }
}
