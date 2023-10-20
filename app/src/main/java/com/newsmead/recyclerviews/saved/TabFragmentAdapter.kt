import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

public class TabFragmentAdapter : FragmentStateAdapter {

    public TabFragmentAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
    ) : super(fragmentManager, lifecycle) {
    
    }
    
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SavedArticlesFragment()
            1 -> SavedListsFragment()
            else -> SavedArticlesFragment()
        }
    }

}