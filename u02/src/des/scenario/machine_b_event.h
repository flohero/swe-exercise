//
// Created by florian on 05.04.21.
//

#pragma once

#include <utility>

#include "machine_event.h"

namespace des {
  class machine_b_event : public machine_event {
  public:

    machine_b_event(time_t t, state_ptr state) :
        machine_event(t, std::move(state)) {}

    std::vector<event_ptr> execute() override {
      auto vec = std::vector<event_ptr>();
      if (!this->state_->buffer_.empty()) {
        auto product = this->state_->buffer_.front();
        product.end_time_ = this->execution_time_;
        this->state_->store_.push_back(product);
        this->state_->buffer_.pop();
      }

      // If buffer is empty stop moving products
      if (this->state_->buffer_.empty()) {
        this->state_->machine_b_ = false;
      } else {
        vec.push_back(
            std::make_shared<machine_b_event>(machine_event::get_random_future_time(),
                                              this->state_)
        );
      }
      return vec;
    }

    [[nodiscard]] bool terminates() const override {
      return false;
    }

  protected:
    [[nodiscard]] std::string details() const override {
      return event::details("Machine B Event");
    }
  };
}

