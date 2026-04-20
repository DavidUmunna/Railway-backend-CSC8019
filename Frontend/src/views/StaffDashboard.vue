  <template>
  <div class="staff-dashboard">
    <header class="dashboard-header">
      <div class="brand-title">
        <div class="brand-mark">W</div>
        <div>
          <h1>whistlestop coffee hut</h1>
          <p>Staff console | Cramlington Station</p>
        </div>
      </div>

      <div class="header-actions">
        <button class="icon-btn" title="Notifications"><Bell size="18" /></button>
        <button class="icon-btn" title="Messages"><MessageSquare size="18" /></button>
        <button class="profile-btn" @click="$emit('logout')">
          <UserCircle2 size="18" /> Logout
        </button>
      </div>
    </header>

    <div class="dashboard-body">
      <aside class="dashboard-sidebar">
        <div class="profile-card">
          <div class="avatar"><User size="24" /></div>
          <div>
            <h2>{{ staffUser.name }}</h2>
            <p>{{ staffUser.role }}</p>
          </div>
        </div>

        <nav class="sidebar-nav">
          <button class="nav-item" :class="{ active: currentView === 'dashboard' }" @click="showDashboardView">
            <Grid size="18" /> Dashboard
          </button>
          <button class="nav-item" :class="{ active: currentView === 'orders' }" @click="toggleOrdersDropdown">
            <Bell size="18" /> Orders
            <span class="dropdown-caret">{{ ordersDropdownOpen ? '▴' : '▾' }}</span>
          </button>
          <div v-if="ordersDropdownOpen" class="orders-dropdown">
            <button class="sub-nav-item" :class="{ active: selectedOrderGroup === 'accepted' }" @click="selectOrderGroup('accepted')">
              Accepted Orders
            </button>
            <button class="sub-nav-item" :class="{ active: selectedOrderGroup === 'inprogress' }" @click="selectOrderGroup('inprogress')">
              In Progress Orders
            </button>
            <button class="sub-nav-item" :class="{ active: selectedOrderGroup === 'completed_cancelled' }" @click="selectOrderGroup('completed_cancelled')">
              Completed / Cancelled
            </button>
          </div>
        </nav>
      </aside>

      <section class="dashboard-main">
        <template v-if="currentView === 'dashboard'">
        <div class="search-panel">
          <div class="search-box">
            <Search size="18" />
            <input type="text" placeholder="Search orders, tasks, staff" />
          </div>
          <div class="summary-pill">Active shift: Morning</div>
        </div>

        <div class="overview-grid">
          <div class="metric-card">
            <p>Orders Today</p>
            <strong>24</strong>
          </div>
          <div class="metric-card">
            <p>Pending Tasks</p>
            <strong>8</strong>
          </div>
          <div class="metric-card">
            <p>Ready to Serve</p>
            <strong>12</strong>
          </div>
        </div>

        <div class="dashboard-panels">
          <div class="panel large-panel">
            <div class="panel-header">
              <h2>Overview</h2>
              <button class="panel-action">See all</button>
            </div>
            <p class="panel-copy">Track station activity, orders, and shift progress in real time for Cramlington Station.</p>
            <div class="counters-row">
              <div class="counter-card">
                <h3>56</h3>
                <p>Orders processed</p>
              </div>
              <div class="counter-card">
                <h3>10</h3>
                <p>Interview schedule</p>
              </div>
              <div class="counter-card">
                <h3>150</h3>
                <p>Profile visited</p>
              </div>
            </div>
          </div>

          <div class="panel small-panel">
            <div class="panel-header">
              <h2>Staff Activity</h2>
            </div>
            <div class="activity-row">
              <div class="activity-item"><strong>50%</strong><span>Orders completed</span></div>
              <div class="activity-item"><strong>50%</strong><span>Shift uptime</span></div>
              <div class="activity-item"><strong>50%</strong><span>Customer ready</span></div>
            </div>
            <div class="activity-list">
              <div class="activity-note">Your shift starts at 07:00.</div>
              <div class="activity-note">New order assigned to barista station.</div>
            </div>
          </div>
        </div>
        </template>

        <template v-else>
          <div class="panel orders-workflow-panel">
            <div class="panel-header">
              <h2>{{ activeOrdersHeading }}</h2>
              <button class="panel-action" @click="loadOrders" :disabled="ordersLoading">
                {{ ordersLoading ? 'Refreshing...' : 'Refresh' }}
              </button>
            </div>

            <p class="panel-copy">
              Staff can only move orders currently in <strong>ACCEPTED</strong> status to <strong>IN_PROGRESS</strong>.
            </p>

            <div v-if="ordersLoading" class="orders-state">Loading orders...</div>
            <div v-else-if="ordersError" class="orders-state error">{{ ordersError }}</div>
            <div v-else-if="filteredOrders.length === 0" class="orders-state">No {{ activeOrdersHeading.toLowerCase() }} found.</div>

            <div v-else class="orders-list">
              <article v-for="order in filteredOrders" :key="order.id" class="order-row">
                <div class="order-row-main">
                  <h3>#{{ order.id }}</h3>
                  <p>
                    {{ order.orderDate || 'No date' }} | Pickup: {{ order.pickupTime || '--:--' }} | Total: ${
                    formatTotal(order.totalAmount)
                    }
                  </p>
                </div>

                <div class="order-row-actions">
                  <span class="status-chip" :class="statusClass(order.status)">{{ order.status }}</span>
                  <button
                    class="panel-action update-btn"
                    :disabled="order.status !== 'ACCEPTED' || updatingOrderId === order.id"
                    @click="moveAcceptedToInProgress(order.id)"
                  >
                    {{ updatingOrderId === order.id ? 'Updating...' : 'Move to IN_PROGRESS' }}
                  </button>
                </div>
              </article>
            </div>
          </div>
        </template>
      </section>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import orderService from '../services/orderService.js';

const props = defineProps({
  staffUser: {
    type: Object,
    default: () => ({ name: 'Staff Member', role: 'Station Staff' })
  }
});

const currentView = ref('dashboard');
const staffOrders = ref([]);
const ordersLoading = ref(false);
const ordersError = ref('');
const updatingOrderId = ref(null);
const ordersDropdownOpen = ref(false);
const selectedOrderGroup = ref('accepted');

const activeOrdersHeading = computed(() => {
  if (selectedOrderGroup.value === 'accepted') return 'Accepted Orders';
  if (selectedOrderGroup.value === 'inprogress') return 'In Progress Orders';
  return 'Completed / Cancelled Orders';
});

const filteredOrders = computed(() => {
  if (selectedOrderGroup.value === 'accepted') {
    return staffOrders.value.filter(order => order.status === 'ACCEPTED');
  }

  if (selectedOrderGroup.value === 'inprogress') {
    return staffOrders.value.filter(order => order.status === 'IN_PROGRESS');
  }

  return staffOrders.value.filter(
    order => order.status === 'COMPLETED' || order.status === 'COLLECTED' || order.status === 'CANCELLED'
  );
});

const showDashboardView = () => {
  currentView.value = 'dashboard';
  ordersDropdownOpen.value = false;
};

const toggleOrdersDropdown = async () => {
  ordersDropdownOpen.value = !ordersDropdownOpen.value;
  if (ordersDropdownOpen.value) {
    currentView.value = 'orders';
    await loadOrders();
  }
};

const selectOrderGroup = async (group) => {
  selectedOrderGroup.value = group;
  currentView.value = 'orders';
  if (!staffOrders.value.length) {
    await loadOrders();
  }
};

const loadOrders = async () => {
  try {
    ordersLoading.value = true;
    ordersError.value = '';
    staffOrders.value = await orderService.getAllOrders();
  } catch (error) {
    ordersError.value = error.message || 'Unable to load orders.';
  } finally {
    ordersLoading.value = false;
  }
};

const moveAcceptedToInProgress = async (orderId) => {
  const order = staffOrders.value.find(item => item.id === orderId);
  if (!order || order.status !== 'ACCEPTED') {
    return;
  }

  try {
    updatingOrderId.value = orderId;
    await orderService.updateOrderStatus(orderId, 'IN_PROGRESS');
    order.status = 'IN_PROGRESS';
  } catch (error) {
    ordersError.value = error.message || 'Unable to update order status.';
  } finally {
    updatingOrderId.value = null;
  }
};

const statusClass = (status) => {
  if (status === 'ACCEPTED') return 'accepted';
  if (status === 'IN_PROGRESS') return 'in-progress';
  if (status === 'COMPLETED') return 'completed';
  if (status === 'COLLECTED') return 'collected';
  return 'unknown';
};

const formatTotal = (amount) => Number(amount || 0).toFixed(2);
</script>

<style scoped>
.staff-dashboard,
.staff-dashboard * {
  box-sizing: border-box;
}

.staff-dashboard {
  min-height: 100vh;
  background: #f5f2ef;
  color: #3e2723;
  padding: 24px;
  box-sizing: border-box;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  margin-bottom: 24px;
}

.brand-title {
  display: flex;
  align-items: center;
  gap: 18px;
}

.brand-mark {
  width: 56px;
  height: 56px;
  border-radius: 18px;
  background: #ffbf00;
  display: grid;
  place-items: center;
  font-weight: 800;
  color: #3e2723;
}

.brand-title h1 {
  margin: 0;
  font-size: 24px;
  text-transform: capitalize;
}

.brand-title p {
  margin: 4px 0 0;
  color: #7a665c;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.icon-btn,
.profile-btn {
  border: none;
  border-radius: 14px;
  background: white;
  padding: 10px 10px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #3e2723;
  cursor: pointer;
  box-shadow: 0 8px 20px rgba(0,0,0,0.06);
}

.profile-btn {
  padding: 10px 10px;
}

.dashboard-body {
  display: grid;
  grid-template-columns: minmax(260px, 300px) minmax(0, 1fr);
  gap: 24px;
  align-items: start;
}

.dashboard-sidebar {
  display: grid;
  gap: 24px;
  min-width: 0;
}

.profile-card {
  width: 100%;
  max-width: 100%;
  min-width: 0;
  border-radius: 24px;
  padding: 24px;
  background: white;
  
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.05);
}

.avatar {
  width: 58px;
  height: 58px;
  border-radius: 18px;
  background: #3e2723;
  display: grid;
  place-items: center;
  color: white;
}

.profile-card h2 {
  margin: 0 0 6px;
  font-size: 1rem;
  line-height: 1.3;
  overflow-wrap: anywhere;
}

.profile-card p {
  margin: 0;
  color: #7a665c;
  font-size: 0.85rem;
  line-height: 1.35;
  overflow-wrap: anywhere;
}

.sidebar-nav {
  display: grid;
  gap: 10px;
}

.nav-item {
  border: none;
  border-radius: 20px;
  padding: 10px 14px;
  width: 100%;
  background: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 12px;
  color: #3e2723;
  box-shadow: 0 20px 60px rgba(0,0,0,0.04);
  transition: transform 0.2s ease;
}

.dropdown-caret {
  margin-left: auto;
  font-size: 0.8rem;
}

.orders-dropdown {
  display: grid;
  gap: 8px;
  padding-left: 14px;
}

.sub-nav-item {
  border: none;
  border-radius: 14px;
  padding: 8px 12px;
  background: #fff8e3;
  color: #6f5b52;
  text-align: left;
  cursor: pointer;
  font-size: 0.82rem;
  font-weight: 600;
}

.sub-nav-item.active {
  background: #ffefcc;
  color: #3e2723;
}

.nav-item:hover,
.nav-item.active {
  transform: translateY(-2px);
  background: #ffefcc;
}

.dashboard-main {
  display: grid;
  gap: 24px;
  min-width: 0;
  isolation: isolate;
}

.search-panel {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  align-items: center;
}

.search-box {
  flex: 1 1 360px;
  display: flex;
  align-items: center;
  gap: 12px;
  background: white;
  padding: 16px 18px;
  border-radius: 18px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.05);
}

.search-box input {
  border: none;
  outline: none;
  width: 100%;
  font-size: 1rem;
  color: #3e2723;
  background: transparent;
}

.summary-pill {
  background: white;
  padding: 16px 22px;
  border-radius: 18px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.05);
  white-space: nowrap;
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 20px;
}

.metric-card {
  background: white;
  border-radius: 22px;
  padding: 24px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.05);
  min-width: 0;
}

.metric-card p {
  margin: 0;
  color: #7a665c;
}

.metric-card strong {
  display: block;
  margin-top: 16px;
  font-size: 2rem;
}

.dashboard-panels {
  display: grid;
  grid-template-columns: minmax(0, 2fr) minmax(0, 1fr);
  gap: 20px;
}

.panel {
  background: white;
  border-radius: 24px;
  padding: 24px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.05);
  min-width: 0;
  overflow: hidden;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}

.panel-header h2 {
  margin: 0;
}

.panel-action {
  border: none;
  background: transparent;
  color: #3e2723;
  cursor: pointer;
  font-weight: 700;
}

.panel-copy {
  color: #7a665c;
  line-height: 1.8;
  margin: 18px 0 24px;
}

.counters-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 14px;
}

.counter-card {
  border-radius: 20px;
  padding: 22px;
  background: #f7f2ec;
  min-width: 0;
}

.counter-card h3 {
  margin: 0 0 10px;
  font-size: 2rem;
}

.counter-card p {
  margin: 0;
  color: #7a665c;
}

.activity-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 14px;
}

.metric-card,
.counter-card,
.activity-item,
.activity-note {
  overflow-wrap: anywhere;
}

.activity-item {
  background: #f7f2ec;
  border-radius: 20px;
  padding: 18px;
  min-width: 0;
}

.activity-item strong {
  display: block;
  margin-bottom: 10px;
  font-size: 1.6rem;
}

.activity-list {
  margin-top: 18px;
  display: grid;
  gap: 12px;
}

.activity-note {
  background: #fff8e3;
  padding: 16px;
  border-radius: 16px;
  color: #6f5b52;
}

.orders-workflow-panel {
  display: grid;
  gap: 16px;
}

.orders-state {
  padding: 14px;
  border-radius: 12px;
  background: #fff8e3;
  color: #6f5b52;
}

.orders-state.error {
  background: #fde7e7;
  color: #a13838;
}

.orders-list {
  display: grid;
  gap: 12px;
}

.order-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 14px;
  padding: 14px;
  border-radius: 14px;
  background: #f7f2ec;
}

.order-row-main h3 {
  margin: 0 0 6px;
}

.order-row-main p {
  margin: 0;
  color: #7a665c;
  font-size: 0.9rem;
}

.order-row-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.status-chip {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  padding: 6px 10px;
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.03em;
}

.status-chip.accepted {
  background: #fff3e0;
  color: #ef6c00;
}

.status-chip.in-progress {
  background: #e3f2fd;
  color: #1565c0;
}

.status-chip.completed,
.status-chip.collected {
  background: #e8f5e9;
  color: #2e7d32;
}

.status-chip.unknown {
  background: #f0f0f0;
  color: #616161;
}

.update-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 1280px) {
  .overview-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (min-width: 1280px) {
  .dashboard-body {
    grid-template-columns: 280px minmax(0, 1fr);
    column-gap: 40px;
  }

  .dashboard-sidebar {
    padding-right: 6px;
  }

  .nav-item {
    width: 80%;
    margin-right: 0;
  }

  .overview-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 900px) {
  .dashboard-panels {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 1100px) {
  .dashboard-body {
    grid-template-columns: 1fr;
  }

  .dashboard-sidebar {
    grid-template-columns: repeat(2, minmax(0, 1fr));
    align-items: start;
  }

  .profile-card {
    height: 100%;
  }

  .sidebar-nav {
    height: 100%;
    align-content: start;
  }

  .dashboard-panels {
    grid-template-columns: 1fr;
  }

  .overview-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 780px) {
  .staff-dashboard {
    padding: 16px;
  }

  .dashboard-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 14px;
  }

  .header-actions {
    width: 100%;
    justify-content: flex-start;
    flex-wrap: wrap;
  }

  .dashboard-sidebar {
    grid-template-columns: 1fr;
  }

  .search-panel {
    align-items: stretch;
  }

  .search-box,
  .summary-pill {
    width: 100%;
  }

  .overview-grid,
  .counters-row,
  .activity-row {
    grid-template-columns: 1fr;
  }

  .order-row {
    flex-direction: column;
    align-items: flex-start;
  }

  .order-row-actions {
    width: 100%;
    justify-content: flex-start;
  }
}

@media (max-width: 480px) {
  .staff-dashboard {
    padding: 14px;
  }

  .brand-title {
    gap: 12px;
  }

  .brand-mark {
    width: 48px;
    height: 48px;
    border-radius: 14px;
  }

  .brand-title h1 {
    font-size: 18px;
  }

  .brand-title p {
    font-size: 0.85rem;
  }

  .header-actions {
    gap: 8px;
  }

  .icon-btn,
  .profile-btn {
    padding: 9px 10px;
    border-radius: 12px;
  }

  .profile-card,
  .panel,
  .metric-card,
  .counter-card,
  .activity-item {
    padding: 14px;
  }

  .metric-card strong,
  .counter-card h3,
  .activity-item strong {
    font-size: 1.4rem;
  }

  .search-box,
  .summary-pill {
    width: 100%;
  }
}
</style>
