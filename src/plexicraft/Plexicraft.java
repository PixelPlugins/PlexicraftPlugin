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

public class Plexicraft extends EZPlugin {
  
  private static HashMap<String, Location> ownedLand = 
	new HashMap<String, Location>();
  
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
			Player me = (Player)caller;
			Location loc = me.getLocation();
			
			Location tr = new Location(loc.getX() + 10, loc.getY(), loc.getZ() - 10);
			Location bl = new Location(loc.getX() - 10, loc.getY(), loc.getZ() + 10);
			
			ownedLand.put(args[1] + "A", tr);
			ownedLand.put(args[1] + "B", bl);
			
			me.chat(Double.toString(ownedLand.get(args[1] + "A").getX()) + Double.toString(ownedLand.get(args[1] + "B").getX()));
		}
	}
}
