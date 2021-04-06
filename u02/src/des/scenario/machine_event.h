//
// Created by florian on 05.04.21.
//

#pragma once

#include <memory>
#include <utility>

namespace des {
  class machine_event : public event {
  public:

  protected:
    struct product {
      time_t creation_time_ = time(nullptr);
      time_t end_time_ = time(nullptr);
    };
    /**
     * Struct to document the state of the machines
     */
    struct machine_state {
      machine_state() = default;

      bool machine_a_ = false;
      bool machine_b_ = false;

      std::queue<product> buffer_ = std::queue<product>();
      std::vector<product> store_ = std::vector<product>();
    };

    using state_ptr = std::shared_ptr<machine_state>;

    machine_event(time_t t, state_ptr state) :
        event(t),
        state_(std::move(state)) {}

    state_ptr state_;
    static const int max_buffer_ = 10;

    [[nodiscard]] time_t get_random_future_time() const {
      return this->execution_time_ + (rand() % 100);
    }

  };
}

