package de.xxschrandxx.npg.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.xxschrandxx.npg.NetherPortalGate;
import de.xxschrandxx.npg.api.*;

public class CMDConfig {
  public static boolean cmd(CommandSender sender, String[] args) {
    if (API.hasPermission(sender, "permissions.command.config")) {
      if (args.length != 1) {
        if (args[1].equalsIgnoreCase("Load")) {
          if (args.length != 2) {
            if (args[2].equalsIgnoreCase("all")) {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.load.message").replace("%config%", "Config"));
              API.loadConfig();;
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.load.message").replace("%config%", "Message"));
              API.loadMessage();
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.load.message").replace("%config%", "Portals"));
              API.loadAllPortals();
              return true;
            }
            else if (args[2].equalsIgnoreCase("config")) {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.load.message").replace("%config%", "Config"));
              API.loadConfig();
              return true;
            }
            else if (args[2].equalsIgnoreCase("message")) {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.load.message").replace("%config%", "Message"));
              API.loadMessage();
              return true;
            }
            else if (args[2].equalsIgnoreCase("portals")) {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.load.message").replace("%config%", "Portals"));
              API.loadAllPortals();
              return true;
            }
            else {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.load.usage"));
              return true;
            }
          }
          else {
            NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.load.usage"));
            return true;
          }
        }
        else if (args[1].equalsIgnoreCase("Save")) {
          if (args.length != 2) {
            if (args[2].equalsIgnoreCase("all")) {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.save.message").replace("%config%", "Config"));
              API.saveConfig();
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.save.message").replace("%config%", "Message"));
              API.saveMessage();
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.save.message").replace("%config%", "Portals"));
              API.saveAllPortals();
              return true;
            }
            else if (args[2].equalsIgnoreCase("config")) {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.save.message").replace("%config%", "Config"));
              API.saveConfig();
              return true;
            }
            else if (args[2].equalsIgnoreCase("message")) {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.save.message").replace("%config%", "Message"));
              API.saveMessage();
              return true;
            }
            else if (args[2].equalsIgnoreCase("portals")) {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.save.message").replace("%config%", "Portals"));
              API.saveAllPortals();
              return true;
            }
            else {
              NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.save.usage"));
              return true;
            }
          }
          else {
            NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.save.usage"));
            return true;
          }
        }
        else {
          NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.usage"));
          return true;
        }
      }
      else {
        NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("command.config.usage"));
        return true;
      }
    }
    else {
      NetherPortalGate.getCommandSenderHandler().sendMessage(sender, API.getMessage().getString("nopermission").replace("%permission%", API.getConfig().getString("permissions.command.config")));
      return true;
    }
  }

  public static List<String> list(CommandSender sender, String[] args) {
    List<String> list = new ArrayList<String>();
    if (API.hasPermission(sender, "permissions.command.config")) {
      if (args.length == 1) {
        list.add("config");
      }
      if ((args.length == 2) && args[0].equalsIgnoreCase("config")) {
        list.add("load");
        list.add("save");
      }
      if ((args.length == 3) && args[0].equalsIgnoreCase("config") && (args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("save"))) {
        list.add("all");
        list.add("config");
        list.add("message");
        list.add("portals");
      }
    }
    return list;
  }
}
