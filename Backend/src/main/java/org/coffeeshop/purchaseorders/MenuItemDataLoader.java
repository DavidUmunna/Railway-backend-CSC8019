package org.coffeeshop.purchaseorders;

import org.coffeeshop.purchaseorders.models.MenuItem;
import org.coffeeshop.purchaseorders.models.MenuItemSize;
import org.coffeeshop.purchaseorders.models.MenuItemType;
import org.coffeeshop.purchaseorders.repositories.MenuItemRepository;
import org.coffeeshop.purchaseorders.repositories.MenuItemTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * @ModifiedBy Umunna David
 * added component annotation to enable automatic execution on application startup
 */
@Component
/**
 * Seeds the database with initial menu items and their size variants on application startup.
 * Only inserts data if the menu item table is empty.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-18
 */
public class MenuItemDataLoader implements CommandLineRunner {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemTypeRepository menuItemTypeRepository;

    /**
     * Constructs the data loader with the required repositories.
     *
     * @param menuItemRepository     the repository for menu item entities
     * @param menuItemTypeRepository the repository for menu item type entities
     */
    public MenuItemDataLoader(MenuItemRepository menuItemRepository, MenuItemTypeRepository menuItemTypeRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemTypeRepository = menuItemTypeRepository;
    }

    /**
     * Seeds the database with predefined menu items and their size variants.
     * Only inserts data if the menu item table is currently empty.
     *
     * @param args application startup arguments (unused)
     */
    @Override
    public void run(String... args) {
        if (menuItemRepository.count() > 0) {
            return;
        }

        MenuItem americano = new MenuItem(1L, "Americano", "A smooth espresso diluted with hot water for a rich, classic black coffee.", true);
        MenuItem americanoWithMilk = new MenuItem(2L, "Americano with milk", "An Americano topped with a splash of milk for a smoother taste.", true);
        MenuItem latte = new MenuItem(3L, "Latte", "Creamy espresso mixed with steamed milk and a light layer of foam.", true);
        MenuItem cappuccino = new MenuItem(4L, "Cappuccino", "A balanced blend of espresso, steamed milk, and thick milk foam.", true);
        MenuItem hotChocolate = new MenuItem(5L, "Hot Chocolate", "Warm, creamy milk blended with rich chocolate.", true);
        MenuItem mocha = new MenuItem(6L, "Mocha", "Espresso combined with chocolate and steamed milk for a sweet, coffee treat.", true);
        MenuItem mineralWater = new MenuItem(7L, "Mineral water", "Refreshing still bottled water.", true);

        menuItemRepository.save(americano);
        menuItemRepository.save(americanoWithMilk);
        menuItemRepository.save(latte);
        menuItemRepository.save(cappuccino);
        menuItemRepository.save(hotChocolate);
        menuItemRepository.save(mocha);
        menuItemRepository.save(mineralWater);

        menuItemTypeRepository.save(new MenuItemType(americano, MenuItemSize.Regular, 1.50, true));
        menuItemTypeRepository.save(new MenuItemType(americano, MenuItemSize.Large, 2.00, true));
        menuItemTypeRepository.save(new MenuItemType(americanoWithMilk, MenuItemSize.Regular, 2.00, true));
        menuItemTypeRepository.save(new MenuItemType(americanoWithMilk, MenuItemSize.Large, 2.50, true));
        menuItemTypeRepository.save(new MenuItemType(latte, MenuItemSize.Regular, 2.50, true));
        menuItemTypeRepository.save(new MenuItemType(latte, MenuItemSize.Large, 3.00, true));
        menuItemTypeRepository.save(new MenuItemType(cappuccino, MenuItemSize.Regular, 2.50, true));
        menuItemTypeRepository.save(new MenuItemType(cappuccino, MenuItemSize.Large, 3.00, true));
        menuItemTypeRepository.save(new MenuItemType(hotChocolate, MenuItemSize.Regular, 2.00, true));
        menuItemTypeRepository.save(new MenuItemType(hotChocolate, MenuItemSize.Large, 2.50, true));
        menuItemTypeRepository.save(new MenuItemType(mocha, MenuItemSize.Regular, 2.50, true));
        menuItemTypeRepository.save(new MenuItemType(mocha, MenuItemSize.Large, 3.00, true));
        menuItemTypeRepository.save(new MenuItemType(mineralWater, MenuItemSize.Regular, 1.00, true));
    }

    
}