package Main.Runnable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/** Criada uma interface com a função de delegar para o Sheduler um novo task, com o mesmo resultado de
 *  new BukkitRunnable(){
 *      public void run(){
 *
 *      }
 *  }
 *
 * porém feito de uma forma mais sutil usando esta interface funcional.
 *
 * ((BukkitRunnable) () -> {
 *
 * })
 *
 */

@FunctionalInterface
public interface BukkitRunnable extends Runnable {
    int taskId = -1;

    default public void cancel() throws IllegalStateException {
        Bukkit.getScheduler().cancelTask(this.getTaskId());
    }

    default BukkitTask runTask(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTask(plugin, this));
    }

    default BukkitTask runTaskAsynchronously(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskAsynchronously(plugin, this));
    }

    default BukkitTask runTaskLater(Plugin plugin, int delay) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskLater(plugin, this, delay));
    }

    default BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay));
    }

    default BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period));
    }

    default BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
        this.checkState();
        return this.setupId(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, period));
    }

    default int getTaskId() throws IllegalStateException {
        int id = this.taskId;
        if (id == -1) {
            throw new IllegalStateException("Not scheduled yet");
        } else {
            return id;
        }
    }

    default void checkState() {
        if (this.taskId != -1) {
            throw new IllegalStateException("Already scheduled as " + this.taskId);
        }
    }

    default BukkitTask setupId(BukkitTask task) {
        return task;
    }
}
