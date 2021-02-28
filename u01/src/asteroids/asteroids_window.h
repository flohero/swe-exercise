#include <ml5/ml5.h>
#include "spaceship.h"
#pragma once

constexpr int TICK_INTERVAL = 10;

class asteroids_window: public ml5::window {
public:
	using context_t = ml5::paint_event::context_t;

	void on_init() override {
		set_prop_background_brush(*wxBLACK_BRUSH);
		start_timer(std::chrono::milliseconds{ TICK_INTERVAL });
		this->ship = spaceship{this->get_width() / 2, this->get_height() / 2};
	}

	void on_paint(ml5::paint_event const& event) override {
		
		context_t &ctx = event.get_context();
		ship.draw(ctx);
	}

private:
	spaceship ship;
};

