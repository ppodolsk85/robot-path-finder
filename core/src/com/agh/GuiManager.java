package com.agh;

import com.badlogic.gdx.Gdx;

/**
 * Created by pete on 25.11.14.
 */
public class GuiManager {
    private enum ManagerState {ADD_POLYGON, ADD_START, ADD_END};

    private ManagerState currentState = ManagerState.ADD_POLYGON;

    public void setAddPolygonState() {
        currentState = ManagerState.ADD_POLYGON;
        Gdx.app.log("GuiManager", "add polygon");
    }

    public void setAddStartState() {
        currentState = ManagerState.ADD_START;
        Gdx.app.log("GuiManager", "add start");
    }

    public void setAddEndState() {
        currentState = ManagerState.ADD_END;
        Gdx.app.log("GuiManager", "add end");
    }
}
