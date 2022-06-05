# adventure4spigot
Adventure4Spigot is a small utility that aims to integrate Adventure's component API throughout Bukkit, the same way Paper does.

This library is a WIP. Due to the large use of Adventure in Paper, it may be impossible to provide abstractions for every method out there, so only the most important ones may be implemented.

To-do:
- [x] ItemMeta's display name and lore
- [x] Inventory titles
- [ ] Scoreboard teams
- [ ] Scoreboard objectives
- [ ] Signs
- [ ] Permission messages
- [ ] Entity display names
- [ ] Reasons (kicks and bans)
- [ ] Events
- [ ] Books
- [ ] Maps
- [ ] Avoid using reflections when Paper is present
- [x] Better documentation on API methods

## Examples

### Create an inventory
```java
Inventory inv = SpigotAdventure.createInventory(null, 9, Component.text()
        .content("Hello!")
        .color(NamedTextColor.AQUA)
        .build()
);
event.getPlayer().openInventory(inv);
```

### Set item's display name
```java
ItemStack item = new ItemStack(Material.DIAMOND);
ItemMeta meta = item.getItemMeta();
SpigotAdventure.setDisplayName(meta, Component.text("Hello!"));
item.setItemMeta(meta);
```