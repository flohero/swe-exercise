//
// Created by florian on 27.03.21.
//

#pragma once

#include <ostream>
#include <chrono>
#include <memory>
#include <vector>

namespace des {
  class event {
  public:

    using event_ptr = std::shared_ptr<event>;

    friend std::ostream &operator<<(std::ostream &os, const event &e) {
      return os << e.details();
    }

    friend bool operator<(const event &e1, const event &e2) {
      return e1.execution_time_ < e2.execution_time_;
    }

    friend bool operator>(const event &e1, const event &e2) {
      return e1.execution_time_ > e2.execution_time_;
    }

    struct greater_than_comparator {
      bool operator()(const event_ptr & left, const event_ptr & right) {
        return *left > *right;
      }
    };

    explicit event(std::time_t execution_time) :
        execution_time_{execution_time} {}

    virtual std::vector<event_ptr> execute() = 0;

    [[nodiscard]] virtual bool terminates() const = 0;

  protected:
    std::time_t execution_time_;

    [[nodiscard]] virtual std::string details() const = 0;

    /**
     * Simple details function
     * @param name the name of the event
     * @returns the eventname plus the timestamp
     */
    [[nodiscard]] std::string details(const std::string &name) const {
      return name + "@" + std::to_string(this->execution_time_);
    }
  };
}

