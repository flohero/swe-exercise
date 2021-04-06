//
// Created by florian on 06.04.21.
//

#pragma once

#include <sstream>

namespace des {
  class end_event : public machine_event {
  public:
    explicit end_event(state_ptr state) :
        machine_event(time(nullptr), std::move(state)) {}

    [[nodiscard]] bool terminates() const override {
      return true;
    }

    [[nodiscard]] std::vector<event_ptr> execute() override {
      return std::vector<event_ptr>();
    }

  protected:
    [[nodiscard]] std::string details() const override {
      time_t total = 0;
      for (const auto &product : this->state_->store_) {
        total += product.end_time_ - product.creation_time_;
      }
      unsigned long avg = static_cast<unsigned long>(total) / this->state_->store_.size();
      std::stringstream stream;
      const std::string separator = "----------------------------";
      stream << event::details("End Event") << std::endl
             << separator << std::endl
             << "Final Stats:" << std::endl
             << "Average Time of Production: " << std::to_string(avg) << "s" << std::endl
          << separator << std::endl;
      return stream.str();
    }
  };
}

