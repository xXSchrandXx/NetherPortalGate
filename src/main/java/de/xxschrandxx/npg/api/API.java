package de.xxschrandxx.npg.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import de.xxschrandxx.api.minecraft.Config;
import de.xxschrandxx.npg.NetherPortalGate;
import de.xxschrandxx.npg.api.config.*;

public class API {

  public static FileConfiguration getConfig() {
    return Storage.config.get();
  }

  public static void saveConfig() {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.saveConfig | Saving config.");
    Storage.config.save();
  }

  public static void loadConfig() {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.loadConfig | Loading config.");
    Storage.config.reload();
  }

  public static FileConfiguration getMessage() {
    return Storage.message.get();
  }

  public static void saveMessage() {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.saveMessage | Saving messages.");
    Storage.message.save();
  }

  public static void loadMessage() {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.loadMessage | Loading messages.");
    Storage.message.reload();
  }

  public static ConcurrentHashMap<UUID, Portal> listPortals() {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.listPortals | Listing Portals");
    return Storage.portale;
  }

  public static ConcurrentHashMap<UUID, Portal> listPortalsWithName(String String) {
    ConcurrentHashMap<UUID, Portal> p = new ConcurrentHashMap<UUID, Portal>();
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.listPortalsWithName | Listing Portals with Name: " + String);
    for (Entry<UUID, Portal> po : listPortals().entrySet()) {
      if (po.getValue().getName().equals(String)) {
        NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.listPortalsWithName | Adding Portal: " + po.getKey());
        p.put(po.getKey(), po.getValue());
      }
    }
    if (p.isEmpty()) {
      NetherPortalGate.getLogHandler().log(true, Level.WARNING, "API.listPortalsWithName | Returns null because List is Empty.");
      return null;
    }
    return p;
  }

  public static ConcurrentHashMap<UUID, Portal> listPortalsWithLocation(Location Location) {
    ConcurrentHashMap<UUID, Portal> p = new ConcurrentHashMap<UUID, Portal>();
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.listPortalsWithLocation | List Portals with Location: " + Location);
    for (Entry<UUID, Portal> po : listPortals().entrySet()) {
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.listPortalsWithLocation | Testing: " + po.getKey());
      for (BlockLocation bs : po.getValue().getLocations()) {
        if (sameLocations(bs.toLocation(), Location)) {
          NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.listPortalsWithLocation | Adding Portal: " + po.getKey());
          p.put(po.getKey(), po.getValue());
        }
      }
    }
    if (p.isEmpty()) {
      NetherPortalGate.getLogHandler().log(true, Level.WARNING, "API.listPortalsWithLocation | Returns null because List is Empty.");
      return null;
    }
    return p;
  }

  public static ConcurrentHashMap<UUID, Portal> listNearbyPortals(Location Location, int Radius) {
    ConcurrentHashMap<UUID, Portal> p = new ConcurrentHashMap<UUID, Portal>();
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.listNearbyPortals | Listing Portals " + Radius + " Blocks near Location: " + Location);
    for(int x = Location.getBlockX() - Radius; x <= Location.getBlockX() + Radius; x++) {
      for(int y = Location.getBlockY() - Radius; y <= Location.getBlockY() + Radius; y++) {
        for(int z = Location.getBlockZ() - Radius; z <= Location.getBlockZ() + Radius; z++) {
          Entry<UUID, Portal> pe = getPortalfromLocation(new Location(Location.getWorld(), x, y, z));
          if (pe != null) {
            NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.listNearbyPortals | Adding Portal: " + pe.getKey());
            p.put(pe.getKey(), pe.getValue());
          }
        }
      }
    }
    if (p.isEmpty()) {
      NetherPortalGate.getLogHandler().log(true, Level.WARNING, "API.listNearbyPortals | Returns null beacause List is Empty.");
      return null;
    }
    return p;
  }

  public static Entry<UUID, Portal> getPortalfromLocation(Location Location) {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.getPortalsfromLocation | Getting a Portal with Location: " + Location);
    Entry<UUID, Portal> p = null;
    ConcurrentHashMap<UUID, Portal> portals = listPortalsWithLocation(Location);
    if (portals != null) {
      if (!portals.isEmpty()) {
        if (portals.size() == 1) {
          for (Entry<UUID, Portal> po : portals.entrySet()) {
            p = po;
            break;
          }
        }
      }
    }
    if (p == null)
      NetherPortalGate.getLogHandler().log(true, Level.WARNING, "API.getPortalsfromLocation | No Portal found, returns null.");
    else
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.getPortalsfromLocation | Returning Portal: " + p.getKey());
    return p;
  }

  public static Entry<UUID, Portal> getPortalfromPortal(Portal Portal) {
    Entry<UUID, Portal> p = null;
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.getPortalfromPortal | Getting a Portal from: " + Portal);
    ConcurrentHashMap<UUID, Portal> portale = listPortalsWithName(Portal.getName());
    if (portale != null) {
      if (!portale.isEmpty()) {
        if (portale.size() == 2) {
          for (Entry<UUID, Portal> po : portale.entrySet()) {
            if (po.getValue() != Portal) {
              p = po;
              break;
            }
          }
        }
      }
    }
    if (p == null)
      NetherPortalGate.getLogHandler().log(true, Level.WARNING, "API.getPortalfromPortal | No Portal found, returns null.");
    else
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.getPortalfromPortal | Returning Portal: " + p.getKey());
    return p;
  }

  public static Portal getPortalfromUUID(UUID UUID) {
    Portal p = null;
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.getPortalfromUUID | Getting the Portal with UUID: " + UUID);
    ConcurrentHashMap<UUID, Portal> portale = listPortals();
    for (Entry<UUID, Portal> pe : portale.entrySet()) {
      if (pe.getKey().equals(UUID)) {
        p = pe.getValue();
        break;
      }
    }
    if (p == null)
      NetherPortalGate.getLogHandler().log(true, Level.WARNING, "API.getPortalfromUUID | No Portal found, returns null.");
    else
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.getPortalfromUUID | Returning Portal: " + UUID);
    return p;
  }

  public static void setPortal(UUID UUID, Portal Portal) {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.setPortal | Adding Portal: " + UUID);
    listPortals().put(UUID, Portal);
    savePortal(UUID, Portal);
  }

  public static void removePortal(UUID UUID) {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.removePortal | Removing Portal: " + UUID);
    listPortals().remove(UUID);
    deletePortalConfig(UUID);
  }

  public static void removePortal(UUID UUID, Portal Portal) {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.removePortal | Removing Portal: " + UUID);
    listPortals().remove(UUID, Portal);
    deletePortalConfig(UUID);
  }

  public static void removePortal(Entry<UUID, Portal> Entry) {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.removePortal | Removing Portal: " + Entry.getKey());
    removePortal(Entry.getKey(), Entry.getValue());
  }

  public static UUID generateUUID() {
    UUID uuid = UUID.randomUUID();
    if (getPortalfromUUID(uuid) != null) {
      uuid = generateUUID();
    }
    return uuid;
  }

  public static boolean sameBlocks(Block Block1, Block Block2) {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.sameBlocks | Testing Block " + Block1 + " and " + Block2);
    if (Block1 == Block2)
      return true;
    if (Block1.equals(Block2))
      return true;
    if (sameLocations(Block1.getLocation(), Block2.getLocation()))
      return true;
    return false;
  }

  public static boolean sameLocations(Location Location1, Location Location2) {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.sameLocations | Testing Location " + Location1 + " and " + Location2);
    if (Location1 == Location2)
      return true;
    if (Location1.equals(Location2))
      return true;
    if (Location1.getWorld() == Location2.getWorld()) {
      double x1 = roundToHalf(Location1.getX());
      double y1 = roundToHalf(Location1.getY());
      double z1 = roundToHalf(Location1.getY());
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.sameLocations | Modified Location1{x=" + x1 + ", y=" + y1 + ", z=" + z1 + "}");
      double x2 = roundToHalf(Location2.getX());
      double y2 = roundToHalf(Location2.getY());
      double z2 = roundToHalf(Location2.getY());
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.sameLocations | Modified Location2{x=" + x2 + ", y=" + y2 + ", z=" + z2 + "}");
      if ((x1 == x2) && (y1 == y2) && (z1 == z2)) 
        return true;
      double xd = Math.abs(Location1.getX() - Location2.getX());
      double yd = Math.abs(Location1.getY() - Location2.getY());
      double zd = Math.abs(Location1.getZ() - Location2.getZ());
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.sameLocations | X-Distance=" + xd + ", Y-Distance=" + yd + ", Z-Distance=" + zd);
      if ((1 >= xd) && (1 >= yd) && (1 >= zd))
        return true;
    }
    return false;
  }

  public static void savePortal(UUID UUID, Portal Portal) {
    File portalconfigfolder = new File(NetherPortalGate.getInstance().getDataFolder(), "portals");
    if (!portalconfigfolder.exists()) {
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "savePortal | Creating portalfolder");
      portalconfigfolder.mkdir();
    }
    File cf = new File(portalconfigfolder + File.separator +  UUID.toString() + ".yml");
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "savePortal | Saving Portal " + UUID + "...");
    Config c = new Config(cf);
    c.reload();
    c.get().set(UUID.toString() + ".name", Portal.getName());
    c.get().set(UUID.toString() + ".exit.world", Portal.getExitWorld());
    c.get().set(UUID.toString() + ".exit.x", Portal.getExitX());
    c.get().set(UUID.toString() + ".exit.y", Portal.getExitY());
    c.get().set(UUID.toString() + ".exit.z", Portal.getExitZ());
    c.get().set(UUID.toString() + ".exit.pitch", Portal.getExitPitch());
    c.get().set(UUID.toString() + ".exit.yaw", Portal.getExitYaw());
    int i = 0;
    for (BlockLocation bl : Portal.getLocations()) {
      c.get().set(UUID.toString() + ".portals." + i + ".world", bl.getWorld());
      c.get().set(UUID.toString() + ".portals." + i + ".x", bl.getX());
      c.get().set(UUID.toString() + ".portals." + i + ".y", bl.getY());
      c.get().set(UUID.toString() + ".portals." + i + ".z", bl.getZ());
      i++;
    }
    c.save();
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "savePortal | Saved Portal: " + UUID);
  }

  public static void savePortal(Entry<UUID, Portal> Entry) {
    savePortal(Entry.getKey(), Entry.getValue());
  }

  public static void saveAllPortals() {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "saveAllPortals | Saving every Portal...");
    for (Entry<UUID, Portal> entry : listPortals().entrySet()) {
      savePortal(entry);
    }
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "saveAllPortals | Saved every Portal.");
  }

  public static Portal loadPortal(Config Config) {
    Portal portal = null;
    UUID uuid = UUID.fromString(Config.getFile().getName().replace(".yml", ""));
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "Loading " + uuid.toString() + "...");
    String name = Config.get().getString(uuid.toString() + ".name");
    String exitworld = Config.get().getString(uuid.toString() + ".exit.world");
    double exitx = Config.get().getDouble(uuid.toString() + ".exit.x");
    double exity = Config.get().getDouble(uuid.toString() + ".exit.y");
    double exitz = Config.get().getDouble(uuid.toString() + ".exit.z");
    float exitpitch = Config.get().getInt(uuid.toString() + ".exit.pitch");
    float exityaw = Config.get().getInt(uuid.toString() + ".exit.yaw");
    ConfigurationSection section = Config.get().getConfigurationSection(uuid.toString() + ".portals");
    List<BlockLocation> locations = new ArrayList<BlockLocation>();
    for (String i : section.getKeys(false)) {
      String world = section.getString(i + ".world");
      double x = section.getDouble(i + ".x");
      double y = section.getDouble(i + ".y");
      double z = section.getDouble(i + ".z");
      locations.add(new BlockLocation(world, x, y, z));
      portal = new Portal(name, locations, exitworld, exitx, exity, exitz, exitpitch, exityaw);
    }
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "Loaded " + uuid.toString() + ".");
    return portal;
  }

  public static Portal loadPortal(File File) {
    Portal portal = null;
    if (File.exists()) {
      Config c = new Config(File);
      portal = loadPortal(c);
    }
    return portal;
  }

  public static Portal loadPortal(UUID UUID) {
    Portal portal = null;
    File f = new File(getPortalFolder()+ File.pathSeparator + UUID.toString() + ".yml");
    if (f.exists()) {
      portal = loadPortal(f);
    }
    return portal;
  }

  public static boolean loadAllPortals() {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.loadAllPortals | Loading all Portals...");
    if (getPortalFolder() != null) {
      if (getPortalFolder().listFiles() != null) {
        if (getPortalFolder().listFiles().length > 0) {
          for (File f : getPortalFolder().listFiles()) {
            Portal portal = loadPortal(f);
            listPortals().put(UUID.fromString(f.getName().replace(".yml", "")), portal);
          }
        }
      }
    }
    if (listPortals().isEmpty()) {
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.loadAllPortals | No existing Portals.");
      return false;
    }
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.loadAllPortals | Loaded all Portals.");
    return true;
  }

  public static void deletePortalConfig(UUID uuid) {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.deletePortalConfig | Deleting Portal: " + uuid + "...");
    File pfolder = getPortalFolder();
    if (pfolder.listFiles().length != 0) {
      for (File pfile : pfolder.listFiles()) {
        UUID uuid2 = UUID.fromString(pfile.getName().replace(".yml", ""));
        NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.deletePortalConfig | Testing Portal: " + uuid2);
        if (uuid.equals(uuid2)) {
          NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.deletePortalConfig | Deleted Portal: " + uuid + ".");
          try {
            pfile.delete();
          }
          catch (SecurityException e) {
            NetherPortalGate.getLogHandler().log(true, Level.WARNING, "API.deletePortalConfig | Delettion of Portal: " + uuid + " failed" + e);
          }
          break;
        }
      }
    }
  }

  public static File getPortalFolder() {
    File portalconfigfolder = new File(NetherPortalGate.getInstance().getDataFolder(), "portals");
    if (!portalconfigfolder.isDirectory()) {
      portalconfigfolder.delete();
    }
    if (!portalconfigfolder.exists()) {
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "savePortal | Creating portalfolder");
      portalconfigfolder.mkdir();
    }
    return portalconfigfolder;
  }

  public static boolean checkFolder() {
    if (getPortalFolder() != null) {
      return true;
    }
    else {
      return false;
    }
  }

  public static boolean hasPermission(CommandSender CommandSender, String String) {
    if (CommandSender instanceof Player) {
      Player p = (Player) CommandSender;
      if (p.hasPermission(getConfig().getString(String))) {
        return true;
      }
      else if (getConfig().getBoolean("permissions.allowops")) {
        if (p.isOp()) {
          return true;
        }
        else {
          return false;
        }
      }
      else {
        return false;
      }
    }
    else if (CommandSender instanceof ConsoleCommandSender) {
      return true;
    }
    else if (CommandSender instanceof BlockCommandSender) {
      return true;
    }
    else if (CommandSender instanceof CommandMinecart) {
      return true;
    }
    else {
      return false;
    }
  }

  public static boolean isInt(String String) {
    try {
      Integer.valueOf(String);
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  public static boolean isUUID(String String) {
    try {
      UUID.fromString(String);
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  public static Location createExitLocation(Player Player, List<BlockState> List) {
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.createExitLocation | Creating Exit...");
    if (Player == null) {
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.createExitLocation | Returned null because Player is null.");
      return null;
    }
    if (List == null) {
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.createExitLocation | Returned null because List is null.");
      return null;
    }
    if (List.isEmpty()) {
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.createExitLocation | Returned null because List is Empty.");      
      return null;
    }
    Location pl = Player.getLocation();
    Location el = null;
    double x = 0;
    double exity = List.get(0).getWorld().getMaxHeight();
    double z = 0;
    for (BlockState bs : List) {
      x = x + bs.getX();
      if (exity > bs.getY())
        exity = bs.getY();
      z = z + bs.getZ();
    }
    double centerx = x/List.size();
    double centerz = z/List.size();
    float exityaw = Player.getLocation().getYaw();
    exityaw = Math.round((exityaw / 90) * 90) + 180;
    float exitpitch = 0;
    //Moving X blocks in directory
    double exitx = roundToHalf(centerx);
    double exitz = roundToHalf(centerz);
    el = new Location(pl.getWorld(), exitx, exity, exitz, exityaw, exitpitch);
    el.add(el.getDirection().multiply(2));
/*
    if (!isSafeLocation(el)) {
      NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.createExitLocation | Returned null because Exit is no safe.");
      return null;
    }
*/
    NetherPortalGate.getLogHandler().log(true, Level.INFO, "API.createExitLocation | Created Exit.");
    return el;
  }

  public static double roundToHalf(double d) {
    return Math.round(d * 2) / 2.0;
  }

  public static boolean betweenFloat(float f,float i1, float i2) {
    if (i1 < i2) {
      if (i1 >= f && f <= i2)
        return true;
      else
        return false;
    }
    else {
      if (i2 >= f && f <= i1)
        return true;
      else
        return false;
    }
  }
}
