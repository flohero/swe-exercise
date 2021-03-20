#pragma once
#include <ml5/ml5.h>
#include "asteroids_window.h"

namespace asteroids {

	class  asteroids_app final : public ml5::application {
	protected:
		[[nodiscard]] std::unique_ptr <ml5::window> make_window() const override {
			return std::make_unique<asteroids_window>();
		}
	};
}
