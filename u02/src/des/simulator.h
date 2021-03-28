//
// Created by florian on 27.03.21.
//

#pragma once

#include <iostream>
#include <queue>
#include <memory>
#include <utility>

namespace des {
  class simulator {
  public:

    class simulator_exception : public std::exception {
    public:
      explicit simulator_exception(std::string msg) : msg_{std::move(msg)} {}

      [[nodiscard]] const char *what() const noexcept override {
        return msg_.c_str();
      }

    private:
      std::string msg_;
    };

    using event_ptr = std::shared_ptr<event>;

    simulator(std::initializer_list<event_ptr> init_list) {
      for (const auto &item : init_list) {
        this->events_.push(item);
      }
    }

    /**
     * Execute all events until queue is empty
     */
    void run() {
      while (!execute_top_event());
      this->stop();
    }

    void step() {
      if (execute_top_event()) {
        this->stop();
      }
    }

  private:
    std::priority_queue<event_ptr,
        std::vector<event_ptr>,
        event::bigger_than_comparator> events_;

    /**
     * Executes the top event in the queue
     * @returns true if there was no event or if an event terminated the simulation
     */
    bool execute_top_event() {
      if (this->events_.empty()) {
        return true;
      }
      event_ptr e = this->events_.top();
      this->events_.pop();

      std::cout << (*e) << std::endl;
      std::vector<event_ptr> new_events = e->execute();
      for (const auto &item: new_events) {
        this->events_.push(item);
      }
      return e->terminates();
    }

    void stop() {
      std::cout << "Terminating Simulation!" << std::endl;
    }
  };
}

