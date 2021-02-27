#include <ml5/gui/window.h>
#include "spaceship.h"
#pragma once

constexpr int TICK_INTERVAL = 10;

class asteroids_window: public ml5::window {
public:

	void on_init() override {
		set_prop_background_brush(*wxBLACK_BRUSH);
		start_timer(std::chrono::milliseconds{ TICK_INTERVAL });
		this->ship = spaceship{};
	}

private:
	spaceship ship;
};

