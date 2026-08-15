package io.github.some_example_name;
// Phase 2 - Menu System
// Phase 2 - Menu System (this is my feature branch)
import com.badlogic.gdx.Game;
import io.github.some_example_name.screens.LoginScreen;
import io.github.some_example_name.model.User;
import io.github.some_example_name.service.StorageService;

public class Main extends Game {
    private StorageService storageService;
    private User currentUser;

    @Override
    public void create() {
        storageService = new StorageService();
        storageService.loadUsers();
        currentUser = storageService.getUser("test");
        if (currentUser == null) {
            currentUser = new User("test", "123", "Tester", "test@test.com", "male");
            storageService.addUser(currentUser);
            storageService.saveUsers();
        }
        setScreen(new LoginScreen(this));
    }

    public StorageService getStorageService() {
        return storageService;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
