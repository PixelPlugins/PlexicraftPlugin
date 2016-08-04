package plexicraft;
import net.canarymod.plugin.Plugin;
import net.canarymod.logger.Logman;
import net.canarymod.Canary;
import net.canarymod.commandsys.*;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.api.entity.living.humanoid.Player;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Block;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import net.visualillusionsent.utils.PropertiesFile;
import net.canarymod.hook.player.BlockDestroyHook;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;

public class Plexicraft extends EZPlugin implements PluginListener{
  
  private static HashMap<String, Location> ownedLand = 
	new HashMap<String, Location>();
	
	PropertiesFile config = getConfig();
  
  @Command(aliases = { "plexi" },
            description = "runs plexicraft functions",
            permissions = { "" },
            toolTip = "/plexi")
  public void plexicraftCommand(MessageReceiver caller, String[] args) {
    if (caller instanceof Player) { 
      Player me = (Player)caller;
	  Location loc = me.getLocation();
      // Put your code after this line:
	  if(args[1].equalsIgnoreCase("sudo")){
		  if(args[2].equalsIgnoreCase("kill")){
			  me.setHealth(0);
			  me.chat("[PLEXICRAFT]: Killed " + me.getDisplayName());
		  }
		  else if(args[2].equalsIgnoreCase("build")){
			  if(args[3].equalsIgnoreCase("platform")){
				  setBlockAt(new Location(loc.getX(), loc.getY() - 1, loc.getZ()), BlockType.Stone);
			  }
		  }
	  }
	  else if(args[1].equalsIgnoreCase("superjump")){
		  loc.setY(loc.getY() + 10);
		  me.teleportTo(loc);
	  }
      // ...and finish your code before this line.
    }
  }
  
  @Command(aliases = {"claim"},
			description = "claims the area that the player is at",
			permissions = {""},
			toolTip = "/claim"
			)
	public void claimCommand(MessageReceiver caller, String[] args){
		if(caller instanceof Player){
			if(!args[1].equalsIgnoreCase("remove")){
				Player me = (Player)caller;
				Location loc = me.getLocation();
				
				Location tr = new Location(loc.getX() + 10, loc.getY(), loc.getZ() - 10);
				Location bl = new Location(loc.getX() - 10, loc.getY(), loc.getZ() + 10);
				
				ownedLand.put(args[1] + "A", tr);
				ownedLand.put(args[1] + "B", bl);
				
				config.setDouble(args[1] + "A" + "X", tr.getX());
				config.setDouble(args[1] + "B" + "X", bl.getX());
				
				config.save();
				
				me.chat(Double.toString(ownedLand.get(args[1] + "A").getX()) + Double.toString(ownedLand.get(args[1] + "B").getX()));
			}
			else if(args[1].equalsIgnoreCase("remove")){
				ownedLand.remove(args[2] + "A");
				ownedLand.remove(args[2] + "B");
			}
		
		}
	}
	
	@Command(aliases = {"check"},
			description = "checks to see if you are in your claim",
			permissions = {""},
			toolTip = "/check"
			)
	public void checkCommand(MessageReceiver caller, String[] args){
		if(caller instanceof Player){
			Player me = (Player)caller;
			Location loc = me.getLocation();
			
			double trx;
			double blx;
			double trz;
			double blz;
			
			trx = ownedLand.get(args[1] + "A").getX();
			blx = ownedLand.get(args[1] + "B").getX();
			trz = ownedLand.get(args[1] + "A").getZ();
			blz = ownedLand.get(args[1] + "B").getZ();
			
			boolean inclaim = checkIfInClaim(trx,blx,trz,blz,loc);
			
			if(inclaim){
				me.chat("You are in your claim.");
			}
			else{
				me.chat("You are outside of your claim.");
			}
		}
	}
	
	@Override
	public boolean enable(){
		Canary.hooks().registerListener(this, this);
		return super.enable();
	}
	
	@HookHandler
	public void onBlockDestroy(BlockDestroyHook event){
		System.out.println("[PLEXICRAFT]: Block broken.");
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		
		player.chat("You're destroying a block!");
		String name = player.getDisplayName();
		if(name == null){
			player.chat("You are null!!");
		}
		player.chat(name);
		
		double trx = ownedLand.get(name + "A").getX();
		double blx = ownedLand.get(name + "B").getX();
		double trz = ownedLand.get(name + "A").getZ();
		double blz = ownedLand.get(name + "B").getZ();
		
		boolean isInOwnClaim = checkIfInClaim(trx, blx, trz, blz, loc);
		
		if(!isInOwnClaim){
			event.setCanceled();
		}
	}
	
	public boolean checkIfInClaim(double trx, double blx, double trz, double blz, Location loc){
		if(loc.getX() > blx && loc.getX() < trx
					&& loc.getZ() < blz && loc.getZ() > trz){
				return true;
		}
		else{
			return false;
		}
	}
}
