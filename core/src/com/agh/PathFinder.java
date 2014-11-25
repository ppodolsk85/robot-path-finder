package com.agh;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class PathFinder extends ApplicationAdapter {
	private Stage stage;
	private BitmapFont font;
	private Skin skin;
	private TextureAtlas buttonAtlas;
	private GuiManager guiManager;

	@Override
	public void create () {
		stage = new Stage();
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		guiManager = new GuiManager();

		font = new BitmapFont();
		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("data/uiskin.atlas"));
		skin.addRegions(buttonAtlas);

		createButtons();
		createSliders();

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	private void createButtons() {
		createButton(0, 0, 100, 50, "Set robot",  new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				guiManager.setAddStartState();
			}
		});

		createButton(0, 50, 100, 50, "Set finish", new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				guiManager.setAddEndState();
			}
		});

		createButton(0, 100, 100, 50, "Add polygon", new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				guiManager.setAddPolygonState();
			}
		});

		createButton(0, 400, 100, 50, "Start animation", new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//TODO: compute and show animation
			}
		});
	}

	private void createButton(int x, int y, int width, int height, String text, EventListener listener) {
		TextButton.TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("default-rect");
		textButtonStyle.down = skin.getDrawable("default-rect-down");
		textButtonStyle.checked = skin.getDrawable("default-pane");
		TextButton button = new TextButton(text, textButtonStyle);
		button.setPosition(x, y);
		button.setWidth(width);
		button.setHeight(height);
		button.addListener(listener);
		stage.addActor(button);
	}

	private void createSliders() {
		createSlider(0, 100, 1, false, 0, 175, 100, "Robot size", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("GuiManager", Float.toString(((Slider) actor).getValue()));
			}
		});
	}

	private void createSlider(float min, float max, float stepSize, boolean vertical, int x, int y, int width, String description, EventListener listener) {
		Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
		sliderStyle.background = skin.getDrawable("default-slider");
		sliderStyle.knob = skin.getDrawable("default-slider-knob");

		Slider slider = new Slider(min, max, stepSize, vertical, sliderStyle);
		slider.setPosition(x, y);
		slider.setWidth(width);
		slider.addListener(listener);
		stage.addActor(slider);
	}


	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}
}
