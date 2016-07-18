package com.ipwnage.fortune;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Fortune extends JavaPlugin implements Listener {
	private File config = new File(getDataFolder(), "config.yml");
	private static final Logger log = Logger.getLogger("Minecraft");
	private List<String> fortunes;
	private Boolean broadcast;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		if(!config.exists()) {
			this.saveDefaultConfig();
			log.info("[Fortune] Didn't find a Fortune configuration. Making one now.");
		}
		fortunes = getConfig().getStringList("fortunes");
		broadcast = getConfig().getBoolean("broadcast");
		log.info("[Fortune] Loaded " + fortunes.size() + " fortunes.");
		
		this.getCommand("fortune").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
				Player player = (Player) sender;
				if(args.length == 1 && args[0].equalsIgnoreCase("reload") && (player.hasPermission("fortune.reload") || player.isOp())) {
					fortunes = getConfig().getStringList("fortunes");
					broadcast = getConfig().getBoolean("broadcast");
					log.info("[Fortune] Loaded " + fortunes.size() + " fortunes.");
					sender.sendMessage(ChatColor.YELLOW + "Loaded " + fortunes.size() + " fortunes.");
				} else if (args.length == 1 && args[0].equalsIgnoreCase("reload") && !(player.hasPermission("fortune.reload") || player.isOp())) {
					sender.sendMessage(ChatColor.RED + "You don't have permission.");
				} else {
					Random random = new Random();
					int random_fortune = random.nextInt(fortunes.size());
					String fortune;
					if(broadcast) {
						getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', fortunes.get(random_fortune).replace("{player}", player.getName())));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', fortunes.get(random_fortune).replace("{player}", player.getName())));
						
					}

				}
				return true;
			}
	    });
	}
	
}
